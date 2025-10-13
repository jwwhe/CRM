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
                .like(StringUtils.isNotEmpty(queryDTO.getPhone()),"customer_name",queryDTO.getPhone())
                .eq(queryDTO.getStatus() != null, "status", queryDTO.getStatus())
                .eq(userid != null, "user_id", userid);
        Page<TbCustomer> page = this.page(queryDTO.page(),wrapper);
        return new PageUtils(page);
    }

    @Override
    public PageUtils getPublicCustomerList(TbCustomerQueryDTO queryDTO) {
        QueryWrapper<TbCustomer> wrapper = new QueryWrapper<TbCustomer>().like(StringUtils.isNotEmpty(queryDTO.getCustomerName()),"customer_name",queryDTO.getCustomerName())
                .like(StringUtils.isNotEmpty(queryDTO.getPhone()),"customer_name",queryDTO.getPhone())
                .eq(queryDTO.getStatus() != null, "status", queryDTO.getStatus());
        Page<TbCustomer> page = this.page(queryDTO.page(),wrapper);
        return new PageUtils(page);
    }
}
