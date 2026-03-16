package com.hxw.wscrm.pages.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hxw.wscrm.common.execption.BaseResultCodeEnum;
import com.hxw.wscrm.common.execption.BizException;
import com.hxw.wscrm.common.util.PageUtils;
import com.hxw.wscrm.pages.common.SecurityUtil;
import com.hxw.wscrm.pages.entity.TbCustomer;
import com.hxw.wscrm.pages.entity.TbOrder;
import com.hxw.wscrm.pages.entity.TbOrderProduct;
import com.hxw.wscrm.pages.entity.TbContractProduct;
import com.hxw.wscrm.pages.mapper.TbOrderMapper;
import com.hxw.wscrm.pages.mapper.TbOrderProductMapper;
import com.hxw.wscrm.pages.mapper.TbContractProductMapper;
import com.hxw.wscrm.pages.model.TbOrederQueryDTO;
import com.hxw.wscrm.pages.service.ITbCustomerService;
import com.hxw.wscrm.pages.service.ITbOrderService;
import com.hxw.wscrm.pages.service.ITbProductService;
import com.hxw.wscrm.pages.entity.TbProduct;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxw.wscrm.sys.entity.SysUser;
import com.hxw.wscrm.sys.service.ISysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TbOrderServiceImpl extends ServiceImpl<TbOrderMapper, TbOrder> implements ITbOrderService {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private TbOrderProductMapper orderProductMapper;

    @Autowired
    private TbContractProductMapper contractProductMapper;

    @Autowired
    private ITbCustomerService customerService;

    @Autowired
    private ITbProductService productService;

    @Override
    public PageUtils queryPage(TbOrederQueryDTO queryDTO) {
        QueryWrapper<TbOrder> wrapper = new QueryWrapper<TbOrder>().like(StringUtils.isNotEmpty(queryDTO.getOrderNumber()),"order_number",queryDTO.getOrderNumber())
                .like(StringUtils.isNotEmpty(queryDTO.getCustomerName()),"customer_name",queryDTO.getCustomerName())
                .eq(StringUtils.isNotEmpty(queryDTO.getStatus()),"status",queryDTO.getStatus());
        if (queryDTO.getUserId() != null) {
            wrapper.eq("user_id", queryDTO.getUserId());
        }
        if (queryDTO.getContractId() != null) {
            wrapper.eq("contract_id", queryDTO.getContractId());
        }
        if (queryDTO.getCustomerId() != null) {
            wrapper.eq("customer_id", queryDTO.getCustomerId());
        }
        Page<TbOrder> page = this.page(queryDTO.page(),wrapper);
        enrichOrderRelations(page.getRecords());
        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateOrder(TbOrder tbOrder) {
        Long userId = SecurityUtil.getUserId();
        boolean isNew = (tbOrder.getOrderId() == null);
        if (isNew) {
            tbOrder.setCreateTime(LocalDateTime.now());
            tbOrder.setUserId(userId);
            // Default status 0: Pending Payment
            if (tbOrder.getStatus() == null) {
                tbOrder.setStatus(0);
            }
        }
        bindCustomerInfo(tbOrder);
        tbOrder.setUpdateTime(LocalDateTime.now());
        this.saveOrUpdate(tbOrder);

        // Handle products
        List<TbOrderProduct> productList = tbOrder.getProductList();
        if (productList != null && !productList.isEmpty()) {
            // If update, remove old products first (simplification)
            if (!isNew) {
                // Restore old stock
                List<TbOrderProduct> oldProducts = orderProductMapper.selectList(new QueryWrapper<TbOrderProduct>().eq("order_id", tbOrder.getOrderId()));
                for (TbOrderProduct oldProd : oldProducts) {
                    if (oldProd.getProductId() != null) {
                        TbProduct tbProduct = productService.getById(oldProd.getProductId());
                        if (tbProduct != null) {
                            int currentStock = tbProduct.getStock() == null ? 0 : tbProduct.getStock();
                            tbProduct.setStock(currentStock + oldProd.getQuantity());
                            productService.updateById(tbProduct);
                        }
                    }
                }
                // Warning: This does not revert contract quantity. Only for simple update.
                orderProductMapper.delete(new QueryWrapper<TbOrderProduct>().eq("order_id", tbOrder.getOrderId()));
            }

            for (TbOrderProduct product : productList) {
                if (product.getProductId() != null) {
                    TbProduct tbProduct = productService.getById(product.getProductId());
                    if (tbProduct != null) {
                        int currentStock = tbProduct.getStock() == null ? 0 : tbProduct.getStock();
                        int qty = product.getQuantity() == null ? 0 : product.getQuantity();
                        if (currentStock < qty) {
                            throw new BizException(BaseResultCodeEnum.ILLEGAL_STATE.getCode(), "产品【" + tbProduct.getProductName() + "】库存不足");
                        }
                        tbProduct.setStock(currentStock - qty);
                        productService.updateById(tbProduct);
                    }
                }

                product.setOrderId(tbOrder.getOrderId());
                product.setCreateTime(LocalDateTime.now());
                orderProductMapper.insert(product);

                // Update Contract Product Quantity only for new orders to avoid double counting on edits
                // Also check if contractId is present
                if (isNew && tbOrder.getContractId() != null && product.getProductId() != null) {
                    QueryWrapper<TbContractProduct> cpWrapper = new QueryWrapper<TbContractProduct>()
                            .eq("contract_id", tbOrder.getContractId())
                            .eq("product_id", product.getProductId());
                    TbContractProduct cp = contractProductMapper.selectOne(cpWrapper);
                    if (cp != null) {
                        int currentOrdered = cp.getOrderedQuantity() == null ? 0 : cp.getOrderedQuantity();
                        cp.setOrderedQuantity(currentOrdered + product.getQuantity());
                        contractProductMapper.updateById(cp);
                    }
                }
            }
        }
    }

    @Override
    public TbOrder getOrderDetail(Integer orderId) {
        TbOrder order = this.getById(orderId);
        if (order != null) {
            enrichOrderRelations(Collections.singletonList(order));
            List<TbOrderProduct> products = orderProductMapper.selectList(new QueryWrapper<TbOrderProduct>().eq("order_id", orderId));
            order.setProductList(products);
        }
        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOrder(Integer orderId) {
        TbOrder order = this.getById(orderId);
        if (order == null) {
            throw new BizException(BaseResultCodeEnum.RESOURCE_NOT_FOUND.getCode(), "订单不存在");
        }
        Integer status = order.getStatus();
        if (status == null) {
            throw new BizException(BaseResultCodeEnum.ILLEGAL_STATE.getCode(), "订单状态异常，无法删除");
        }
        if (status != 0) {
            throw new BizException(BaseResultCodeEnum.ILLEGAL_STATE.getCode(), "仅未支付的订单可以删除");
        }
        
        // Restore stock
        List<TbOrderProduct> oldProducts = orderProductMapper.selectList(new QueryWrapper<TbOrderProduct>().eq("order_id", orderId));
        for (TbOrderProduct oldProd : oldProducts) {
            if (oldProd.getProductId() != null) {
                TbProduct tbProduct = productService.getById(oldProd.getProductId());
                if (tbProduct != null) {
                    int currentStock = tbProduct.getStock() == null ? 0 : tbProduct.getStock();
                    tbProduct.setStock(currentStock + oldProd.getQuantity());
                    productService.updateById(tbProduct);
                }
            }
        }

        // 删除订单产品关联记录
        orderProductMapper.delete(new QueryWrapper<TbOrderProduct>().eq("order_id", orderId));
        // 删除订单记录
        this.removeById(orderId);
    }

    @Override
    public void updateOrderStatus(Integer orderId, Integer status) {
        TbOrder order = this.getById(orderId);
        if (order != null) {
            order.setStatus(status);
            order.setUpdateTime(LocalDateTime.now());
            this.updateById(order);
        }
    }

    @Override
    public void confirmOrder(Integer orderId) {
        updateOrderStatus(orderId, 1);
    }

    @Override
    public void payOrder(Integer orderId) {
        updateOrderStatus(orderId, 2);
    }

    @Override
    public void shipOrder(Integer orderId) {
        updateOrderStatus(orderId, 3);
    }

    @Override
    public void completeOrder(Integer orderId) {
        updateOrderStatus(orderId, 4);
    }

    @Override
    public void cancelOrder(Integer orderId) {
        updateOrderStatus(orderId, 5);
    }

    @Override
    public void refundOrder(Integer orderId) {
        updateOrderStatus(orderId, 6);
    }

    private void bindCustomerInfo(TbOrder tbOrder) {
        if (tbOrder.getCustomerId() != null) {
            TbCustomer customer = customerService.getById(tbOrder.getCustomerId());
            if (customer != null) {
                tbOrder.setCustomerName(customer.getCustomerName());
                tbOrder.setCustomerPhone(customer.getContactPhone());
            }
            return;
        }
        if (StringUtils.isBlank(tbOrder.getCustomerName())) {
            return;
        }
        TbCustomer customer = customerService.lambdaQuery()
                .eq(TbCustomer::getCustomerName, tbOrder.getCustomerName())
                .orderByDesc(TbCustomer::getUpdateTime)
                .last("limit 1")
                .one();
        if (customer != null) {
            tbOrder.setCustomerId(customer.getCustomerId());
            tbOrder.setCustomerName(customer.getCustomerName());
            if (StringUtils.isBlank(tbOrder.getCustomerPhone())) {
                tbOrder.setCustomerPhone(customer.getContactPhone());
            }
        }
    }

    private void enrichOrderRelations(List<TbOrder> orders) {
        if (orders == null || orders.isEmpty()) {
            return;
        }
        Set<Long> userIds = orders.stream()
                .map(TbOrder::getUserId)
                .filter(id -> id != null)
                .collect(Collectors.toSet());
        Map<Long, String> userNameMap = userIds.isEmpty() ? Collections.emptyMap()
                : sysUserService.listByIds(userIds).stream()
                .collect(Collectors.toMap(SysUser::getUserId, SysUser::getUsername, (a, b) -> a));

        Set<Long> customerIds = orders.stream()
                .map(TbOrder::getCustomerId)
                .filter(id -> id != null)
                .collect(Collectors.toSet());
        Map<Long, TbCustomer> customerMap = customerIds.isEmpty() ? Collections.emptyMap()
                : customerService.listByIds(customerIds).stream()
                .collect(Collectors.toMap(TbCustomer::getCustomerId, c -> c, (a, b) -> a));

        for (TbOrder order : orders) {
            if (order.getUserId() != null) {
                order.setUserName(userNameMap.get(order.getUserId()));
            }
            if (order.getCustomerId() != null) {
                TbCustomer customer = customerMap.get(order.getCustomerId());
                if (customer != null) {
                    order.setCustomerName(customer.getCustomerName());
                    order.setCustomerPhone(customer.getContactPhone());
                }
            }
        }
    }
}
