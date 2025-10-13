package com.hxw.wscrm.pages.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hxw.wscrm.common.util.PageUtils;
import com.hxw.wscrm.pages.entity.TbCustomer;
import com.hxw.wscrm.pages.model.TbCustomerQueryDTO;
import com.hxw.wscrm.pages.model.TbOrederQueryDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 小贺
 * @since 2025-09-19
 */
public interface ITbCustomerService extends IService<TbCustomer> {

    PageUtils getMyCustomerList(TbCustomerQueryDTO queryDTO);

    PageUtils getPublicCustomerList(TbCustomerQueryDTO queryDTO);
}
