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

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 小贺
 * @since 2025-09-19
 */
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
    public void saveOrUpdateRole(TbOrder sysRole) {

    }
}
