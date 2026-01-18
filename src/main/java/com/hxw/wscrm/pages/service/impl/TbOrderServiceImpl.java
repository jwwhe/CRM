package com.hxw.wscrm.pages.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hxw.wscrm.common.util.PageUtils;
import com.hxw.wscrm.pages.common.SecurityUtil;
import com.hxw.wscrm.pages.entity.TbOrder;
import com.hxw.wscrm.pages.mapper.TbOrderMapper;
import com.hxw.wscrm.pages.model.TbOrederQueryDTO;
import com.hxw.wscrm.pages.service.ITbOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxw.wscrm.sys.entity.SysRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TbOrderServiceImpl extends ServiceImpl<TbOrderMapper, TbOrder> implements ITbOrderService {

    @Override
    public PageUtils queryPage(TbOrederQueryDTO queryDTO) {
        QueryWrapper<TbOrder> wrapper = new QueryWrapper<TbOrder>().like(StringUtils.isNotEmpty(queryDTO.getOrderNumber()),"order_number",queryDTO.getOrderNumber())
                .like(StringUtils.isNotEmpty(queryDTO.getCustomerName()),"customer_name",queryDTO.getCustomerName())
                .like(StringUtils.isNotEmpty(queryDTO.getStatus()),"status",queryDTO.getStatus());
        Page<TbOrder> page = this.page(queryDTO.page(),wrapper);
        return new PageUtils(page);
    }

    @Override
    public void saveOrUpdateOrder(TbOrder tbOrder) {
        Long userId = SecurityUtil.getUserId();
        if (tbOrder.getOrderId() == null) {
            tbOrder.setCreateTime(LocalDateTime.now());
            tbOrder.setUserId(userId);
        }
        tbOrder.setUpdateTime(LocalDateTime.now());
        this.saveOrUpdate(tbOrder);
    }

    @Override
    public TbOrder getOrderDetail(Integer orderId) {
        return this.getById(orderId);
    }

    @Override
    public void deleteOrder(Integer orderId) {
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
}
