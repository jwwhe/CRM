package com.hxw.wscrm.pages.controller;

import com.hxw.wscrm.common.constant.SystemConstant;
import com.hxw.wscrm.common.util.PageUtils;
import com.hxw.wscrm.pages.entity.TbOrder;
import com.hxw.wscrm.pages.model.TbOrederQueryDTO;
import com.hxw.wscrm.pages.service.ITbOrderService;
import com.hxw.wscrm.sys.entity.SysRole;
import com.hxw.wscrm.sys.model.SysRoleQueryDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pages/tbOrder")
@Api(tags = "订单管理", value = "Order")
public class TbOrderController {

    @Autowired
    private ITbOrderService tbOrderService;

    @ApiOperation(value = "分页查询订单", notes = "分页查询订单")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('order:query')")
    public PageUtils list(@ApiParam(value = "查询条件") TbOrederQueryDTO queryDTO) {
        return tbOrderService.queryPage(queryDTO);
    }

    @ApiOperation(value = "添加或更新订单", notes = "添加或更新订单信息")
    @PostMapping("/saveorupdate")
    @PreAuthorize("hasAuthority('order:add') or hasAuthority('order:update')")
    public String save(@RequestBody TbOrder tbOrder) {
        tbOrderService.saveOrUpdateOrder(tbOrder);
        return "success";
    }

    @ApiOperation(value = "订单详情", notes = "根据ID查询订单详情")
    @GetMapping("/detail")
    @PreAuthorize("hasAuthority('order:query')")
    public TbOrder detail(@ApiParam(value = "订单ID") Integer orderId) {
        return tbOrderService.getOrderDetail(orderId);
    }

    @ApiOperation(value = "删除订单", notes = "根据ID删除订单")
    @GetMapping("/delete")
    @PreAuthorize("hasAuthority('order:delete')")
    public String delete(@ApiParam(value = "订单ID") Integer orderId) {
        tbOrderService.deleteOrder(orderId);
        return SystemConstant.CHECK_SUCCESS;
    }

    @ApiOperation(value = "更新订单状态", notes = "更新订单状态")
    @PostMapping("/updateStatus")
    @PreAuthorize("hasAuthority('order:update')")
    public String updateStatus(@ApiParam(value = "订单ID") Integer orderId,
                               @ApiParam(value = "订单状态") Integer status) {
        tbOrderService.updateOrderStatus(orderId, status);
        return SystemConstant.CHECK_SUCCESS;
    }

    @ApiOperation(value = "确认订单", notes = "确认订单")
    @PostMapping("/confirm")
    @PreAuthorize("hasAuthority('order:update')")
    public String confirm(@ApiParam(value = "订单ID") Integer orderId) {
        tbOrderService.confirmOrder(orderId);
        return SystemConstant.CHECK_SUCCESS;
    }

    @ApiOperation(value = "订单支付", notes = "订单支付")
    @PostMapping("/pay")
    @PreAuthorize("hasAuthority('order:update')")
    public String pay(@ApiParam(value = "订单ID") Integer orderId) {
        tbOrderService.payOrder(orderId);
        return SystemConstant.CHECK_SUCCESS;
    }

    @ApiOperation(value = "订单发货", notes = "订单发货")
    @PostMapping("/ship")
    @PreAuthorize("hasAuthority('order:update')")
    public String ship(@ApiParam(value = "订单ID") Integer orderId) {
        tbOrderService.shipOrder(orderId);
        return SystemConstant.CHECK_SUCCESS;
    }

    @ApiOperation(value = "订单完成", notes = "订单完成")
    @PostMapping("/complete")
    @PreAuthorize("hasAuthority('order:update')")
    public String complete(@ApiParam(value = "订单ID") Integer orderId) {
        tbOrderService.completeOrder(orderId);
        return SystemConstant.CHECK_SUCCESS;
    }

    @ApiOperation(value = "取消订单", notes = "取消订单")
    @PostMapping("/cancel")
    @PreAuthorize("hasAuthority('order:update')")
    public String cancel(@ApiParam(value = "订单ID") Integer orderId) {
        tbOrderService.cancelOrder(orderId);
        return SystemConstant.CHECK_SUCCESS;
    }

    @ApiOperation(value = "订单退款", notes = "订单退款")
    @PostMapping("/refund")
    @PreAuthorize("hasAuthority('order:update')")
    public String refund(@ApiParam(value = "订单ID") Integer orderId) {
        tbOrderService.refundOrder(orderId);
        return SystemConstant.CHECK_SUCCESS;
    }
}
