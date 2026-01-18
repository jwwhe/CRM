package com.hxw.wscrm.pages.controller;

import com.hxw.wscrm.common.constant.SystemConstant;
import com.hxw.wscrm.common.util.PageUtils;
import com.hxw.wscrm.pages.entity.TbCustomer;
import com.hxw.wscrm.pages.model.TbCustomerQueryDTO;
import com.hxw.wscrm.pages.model.TbOrederQueryDTO;
import com.hxw.wscrm.pages.service.ITbCustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;

/**
 *
 * @author 小贺
 * @since 2025-09-19
 */
@Controller
@RequestMapping("/pages/tbCustomer")
@Api(tags = "客户管理", value = "Customer")
public class TbCustomerController {
    @Autowired
    private ITbCustomerService tbCustomerService;

    @ApiOperation(value = "分页查询我的客户", notes = "分页查询我的客户")
    @GetMapping("/my_list")
    public PageUtils getMyCustomerList(@ApiParam(value = "查询条件") TbCustomerQueryDTO queryDTO) {
        return tbCustomerService.getMyCustomerList(queryDTO);
    }

    @ApiOperation(value = "分页查询公海客户", notes = "分页查询公海客户")
    @GetMapping("/pulic_list")
    public PageUtils getPublicCustomerList(@ApiParam(value = "查询条件") TbCustomerQueryDTO queryDTO) {
        return tbCustomerService.getPublicCustomerList(queryDTO);
    }

    @ApiOperation(value = "添加客户", notes = "添加客户信息")
    @PostMapping("/save")
    public String save(@RequestBody TbCustomer customer) {
        tbCustomerService.saveCustomer(customer);
        return SystemConstant.CHECK_SUCCESS;
    }

    @ApiOperation(value = "编辑客户", notes = "编辑客户信息")
    @PostMapping("/update")
    public String update(@RequestBody TbCustomer customer) {
        tbCustomerService.updateCustomer(customer);
        return SystemConstant.CHECK_SUCCESS;
    }

    @ApiOperation(value = "客户详情", notes = "根据ID查询客户详情")
    @GetMapping("/detail")
    public TbCustomer detail(@ApiParam(value = "客户ID") Long customerId) {
        return tbCustomerService.getCustomerDetail(customerId);
    }

    @ApiOperation(value = "删除客户", notes = "根据ID删除客户")
    @GetMapping("/delete")
    public String delete(@ApiParam(value = "客户ID") Long customerId) {
        tbCustomerService.deleteCustomer(customerId);
        return SystemConstant.CHECK_SUCCESS;
    }

    @ApiOperation(value = "客户转入公海", notes = "将客户转入公海池")
    @PostMapping("/toPublic")
    public String toPublic(@ApiParam(value = "客户ID") Long customerId) {
        tbCustomerService.toPublicCustomer(customerId);
        return SystemConstant.CHECK_SUCCESS;
    }

    @ApiOperation(value = "领取客户", notes = "从公海池领取客户")
    @PostMapping("/receive")
    public String receive(@RequestParam("customerId") Long customerId) {
        tbCustomerService.receiveCustomer(customerId);
        return SystemConstant.CHECK_SUCCESS;
    }
}
