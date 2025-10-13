package com.hxw.wscrm.pages.service;

import com.hxw.wscrm.common.util.PageUtils;
import com.hxw.wscrm.pages.entity.TbCustomer;
import com.hxw.wscrm.pages.entity.TbOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hxw.wscrm.pages.model.TbCustomerQueryDTO;
import com.hxw.wscrm.pages.model.TbOrederQueryDTO;
import com.hxw.wscrm.sys.entity.SysRole;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 小贺
 * @since 2025-09-19
 */
public interface ITbOrderService extends IService<TbOrder> {

    PageUtils queryPage(TbOrederQueryDTO queryDTO);

    void saveOrUpdateRole(TbOrder tbOrder);
}
