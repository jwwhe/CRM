package com.hxw.wscrm.pages.controller;

import com.hxw.wscrm.common.util.PageUtils;
import com.hxw.wscrm.pages.model.TbCustomerQueryDTO;
import com.hxw.wscrm.pages.model.TbOrederQueryDTO;
import com.hxw.wscrm.pages.service.impl.TbCustomerServiceImpl;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 小贺
 * @since 2025-09-19
 */
@Controller
@RequestMapping("/pages/tbCustomer")
public class TbCustomerController {
    @Autowired
    private TbCustomerServiceImpl tbCustomerServiceImpl;
    @ApiOperation(value = "分页查询订单",notes = "分页查询订单")
    @GetMapping("/my_list")
    public PageUtils getMyCustomerList(@ApiParam(value = "查询条件") TbCustomerQueryDTO queryDTO) {
        return tbCustomerServiceImpl.getMyCustomerList(queryDTO);
    }
    @ApiOperation(value = "分页查询订单",notes = "分页查询订单")
    @GetMapping("/pulic_list")
    public PageUtils getPublicCustomerList(@ApiParam(value = "查询条件") TbCustomerQueryDTO queryDTO) {
        return tbCustomerServiceImpl.getPublicCustomerList(queryDTO);
    }


}
