package com.hxw.wscrm.pages.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxw.wscrm.common.util.PageUtils;
import com.hxw.wscrm.pages.common.SecurityUtil;
import com.hxw.wscrm.pages.entity.*;
import com.hxw.wscrm.pages.mapper.*;
import com.hxw.wscrm.pages.model.TbCustomerQueryDTO;
import com.hxw.wscrm.pages.service.ITbCustomerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TbCustomerServiceImpl extends ServiceImpl<TbCustomerMapper, TbCustomer> implements ITbCustomerService {

    @Autowired
    private TbFollowRecordMapper followRecordMapper;

    @Autowired
    private TbSalesOpportunityMapper salesOpportunityMapper;

    @Autowired
    private TbContractMapper contractMapper;

    @Autowired
    private TbOrderMapper orderMapper;

    @Autowired
    private TbPaymentMapper paymentMapper;

    @Autowired
    private TbCustomerMergeLogMapper customerMergeLogMapper;

    @Override
    public PageUtils getMyCustomerList(TbCustomerQueryDTO queryDTO) {
            Long userid = SecurityUtil.getUserId();
        QueryWrapper<TbCustomer> wrapper = new QueryWrapper<TbCustomer>().like(StringUtils.isNotEmpty(queryDTO.getCustomerName()),"customer_name",queryDTO.getCustomerName())
                .like(StringUtils.isNotEmpty(queryDTO.getContactPhone()),"contact_phone",queryDTO.getContactPhone())
                .eq(StringUtils.isNotEmpty(queryDTO.getCustomerStatus()), "customer_status", queryDTO.getCustomerStatus());
        
        // 如果userid为null，返回空列表，因为没有当前登录用户
        if (userid != null) {
            wrapper.eq("user_id", userid);
        } else {
            wrapper.eq("user_id", -1); // 不存在的用户ID，确保返回空列表
        }
        
        Page<TbCustomer> page = this.page(queryDTO.page(),wrapper);
        return new PageUtils(page);
    }

    @Override
    public PageUtils getPublicCustomerList(TbCustomerQueryDTO queryDTO) {
        QueryWrapper<TbCustomer> wrapper = new QueryWrapper<TbCustomer>().like(StringUtils.isNotEmpty(queryDTO.getCustomerName()),"customer_name",queryDTO.getCustomerName())
                .like(StringUtils.isNotEmpty(queryDTO.getContactPhone()),"contact_phone",queryDTO.getContactPhone())
                .eq(StringUtils.isNotEmpty(queryDTO.getCustomerStatus()), "customer_status", queryDTO.getCustomerStatus())
                .isNull("user_id"); // 只查询公海客户，即没有负责人的客户
        Page<TbCustomer> page = this.page(queryDTO.page(),wrapper);
        return new PageUtils(page);
    }

    @Override
    public void saveCustomer(TbCustomer customer) {
        // 对于公海客户，userId应该为null
        // 市场调研角色创建的客户默认进入公海
        customer.setCreateTime(LocalDateTime.now());
        customer.setUpdateTime(LocalDateTime.now());
        if (customer.getUserId() == null) {
            customer.setIsPool(1);
            customer.setPoolTime(LocalDateTime.now());
        } else {
            customer.setIsPool(0);
        }
        this.save(customer);
    }

    @Override
    public void updateCustomer(TbCustomer customer) {
        customer.setUpdateTime(LocalDateTime.now());
        this.updateById(customer);
    }

    @Override
    public TbCustomer getCustomerDetail(Long customerId) {
        return this.getById(customerId);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        this.removeById(customerId);
    }

    @Override
    public void toPublicCustomer(Long customerId) {
        TbCustomer customer = this.getById(customerId);
        if (customer != null) {
            customer.setUserId(null);
            customer.setIsPool(1);
            customer.setPoolTime(LocalDateTime.now());
            customer.setUpdateTime(LocalDateTime.now());
            this.updateById(customer); // Use updateById which handles nulls for updateStrategy if configured, or use update(wrapper)
            // But here we want to set userId to null. Mybatis-Plus default update ignores null.
            // TbCustomer entity has @TableField(value = "user_id", updateStrategy = FieldStrategy.IGNORED)
            // So setting userId to null will update it to NULL in DB.
        }
    }

    @Override
    public void receiveCustomer(Long customerId) {
        // 保留原有方法，确保兼容性
        receiveCustomer(customerId, SecurityUtil.getUserId());
    }
    
    @Override
    public void receiveCustomer(Long customerId, Long userId) {
        TbCustomer customer = this.getById(customerId);
        if (customer != null && userId != null) {
            // Check if already has owner? Or allow reassign? Usually receive means from pool.
            // If it has owner, maybe check permissions. For now assume valid operation.
            customer.setUserId(userId);
            customer.setIsPool(0);
            customer.setPoolTime(null);
            customer.setUpdateTime(LocalDateTime.now());
            this.updateById(customer);
        }
    }

    @Override
    public List<TbCustomer> checkDuplicate(String customerName, String contactPhone) {
        QueryWrapper<TbCustomer> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(customerName)) {
            wrapper.eq("customer_name", customerName);
        }
        if (StringUtils.isNotEmpty(contactPhone)) {
            wrapper.or().eq("contact_phone", contactPhone);
        }
        return this.list(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void mergeCustomers(Long mainCustomerId, List<Long> duplicateCustomerIds) {
        // 1. 检查主客户是否存在
        TbCustomer mainCustomer = this.getById(mainCustomerId);
        if (mainCustomer == null) {
            throw new RuntimeException("主客户不存在");
        }

        Long operatorId = SecurityUtil.getUserId();

        // 2. 遍历重复客户，将其相关数据关联到主客户
        for (Long duplicateId : duplicateCustomerIds) {
            // 获取被合并客户信息
            TbCustomer mergedCustomer = this.getById(duplicateId);
            if (mergedCustomer == null) {
                continue;
            }

            // Record log
            TbCustomerMergeLog log = new TbCustomerMergeLog();
            log.setOriginalCustomerId(duplicateId);
            log.setOriginalCustomerName(mergedCustomer.getCustomerName());
            log.setTargetCustomerId(mainCustomerId);
            log.setMergeTime(LocalDateTime.now());
            log.setUserId(operatorId);
            log.setReason("手动合并");
            customerMergeLogMapper.insert(log);

            // 更新跟进记录
            QueryWrapper<TbFollowRecord> followWrapper = new QueryWrapper<>();
            followWrapper.eq("customer_id", duplicateId);
            List<TbFollowRecord> followRecords = followRecordMapper.selectList(followWrapper);
            for (TbFollowRecord record : followRecords) {
                record.setCustomerId(mainCustomerId);
                record.setCustomerName(mainCustomer.getCustomerName());
                followRecordMapper.updateById(record);
            }

            // 更新销售机会
            QueryWrapper<TbSalesOpportunity> opportunityWrapper = new QueryWrapper<>();
            opportunityWrapper.eq("customer_id", duplicateId);
            List<TbSalesOpportunity> opportunities = salesOpportunityMapper.selectList(opportunityWrapper);
            for (TbSalesOpportunity opportunity : opportunities) {
                opportunity.setCustomerId(mainCustomerId);
                opportunity.setCustomerName(mainCustomer.getCustomerName());
                salesOpportunityMapper.updateById(opportunity);
            }

            // 更新合同
            QueryWrapper<TbContract> contractWrapper = new QueryWrapper<>();
            contractWrapper.eq("customer_id", duplicateId);
            List<TbContract> contracts = contractMapper.selectList(contractWrapper);
            for (TbContract contract : contracts) {
                contract.setCustomerId(mainCustomerId);
                contract.setCustomerName(mainCustomer.getCustomerName());
                contractMapper.updateById(contract);
            }

            List<TbOrder> orders = orderMapper.selectList(new QueryWrapper<TbOrder>()
                    .eq("customer_id", duplicateId)
                    .or()
                    .eq("customer_name", mergedCustomer.getCustomerName()));
            for (TbOrder order : orders) {
                order.setCustomerId(mainCustomerId);
                order.setCustomerName(mainCustomer.getCustomerName());
                order.setCustomerPhone(mainCustomer.getContactPhone());
                orderMapper.updateById(order);
            }

            // Update Payments
            QueryWrapper<TbPayment> paymentWrapper = new QueryWrapper<>();
            paymentWrapper.eq("customer_id", duplicateId);
            List<TbPayment> payments = paymentMapper.selectList(paymentWrapper);
            for (TbPayment payment : payments) {
                payment.setCustomerId(mainCustomerId);
                payment.setCustomerName(mainCustomer.getCustomerName());
                paymentMapper.updateById(payment);
            }
        }

        // 3. 删除重复客户
        this.removeByIds(duplicateCustomerIds);
    }
}
