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

    void saveOrUpdateOrder(TbOrder tbOrder);

    TbOrder getOrderDetail(Integer orderId);

    void deleteOrder(Integer orderId);

    void updateOrderStatus(Integer orderId, Integer status);

    void confirmOrder(Integer orderId);

    void payOrder(Integer orderId);

    void shipOrder(Integer orderId);

    void completeOrder(Integer orderId);

    void cancelOrder(Integer orderId);

    void refundOrder(Integer orderId);
}
