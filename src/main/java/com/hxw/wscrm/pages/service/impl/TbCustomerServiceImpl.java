package com.hxw.wscrm.pages.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxw.wscrm.common.util.PageUtils;
import com.hxw.wscrm.pages.common.SecurityUtil;
import com.hxw.wscrm.pages.entity.TbCustomer;
import com.hxw.wscrm.pages.entity.TbOrder;
import com.hxw.wscrm.pages.mapper.TbCustomerMapper;
import com.hxw.wscrm.pages.model.TbCustomerQueryDTO;
import com.hxw.wscrm.pages.model.TbOrederQueryDTO;
import com.hxw.wscrm.pages.service.ITbCustomerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.security.Security;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 小贺
 * @since 2025-09-19
 */
@Service
public class TbCustomerServiceImpl extends ServiceImpl<TbCustomerMapper, TbCustomer> implements ITbCustomerService {

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
        Long userId = SecurityUtil.getUserId();
        customer.setUserId(userId);
        customer.setCreateTime(LocalDateTime.now());
        customer.setUpdateTime(LocalDateTime.now());
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
            customer.setUpdateTime(LocalDateTime.now());
            this.updateById(customer);
        }
    }

    @Override
    public void receiveCustomer(Long customerId) {
        Long userId = SecurityUtil.getUserId();
        // 只有当获取到用户id时，才能执行领取操作
        if (userId == null) {
            return;
        }
        TbCustomer customer = this.getById(customerId);
        if (customer != null && customer.getUserId() == null) {
            customer.setUserId(userId);
            customer.setUpdateTime(LocalDateTime.now());
            this.updateById(customer);
        }
    }
}
