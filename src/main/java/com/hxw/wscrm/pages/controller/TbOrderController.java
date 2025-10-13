package com.hxw.wscrm.pages.controller;

import com.hxw.wscrm.common.util.PageUtils;
import com.hxw.wscrm.pages.entity.TbOrder;
import com.hxw.wscrm.pages.model.TbOrederQueryDTO;
import com.hxw.wscrm.pages.service.impl.TbOrderServiceImpl;
import com.hxw.wscrm.sys.entity.SysRole;
import com.hxw.wscrm.sys.model.SysRoleQueryDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
@Api(tags = "订单",value = "order")
@Controller
@RequestMapping("/pages/tbOrder")
public class TbOrderController {

    @Autowired
    private TbOrderServiceImpl tbOrderServiceImpl;
    @ApiOperation(value = "分页查询订单",notes = "分页查询订单")
    @GetMapping("/list")
    public PageUtils list(@ApiParam(value = "查询条件") TbOrederQueryDTO queryDTO) {
        return tbOrderServiceImpl.queryPage(queryDTO);
    }
    @ApiOperation(value = "添加角色",notes = "添加角色信息")
    @PostMapping("/saveorupdate")
    public String save(@RequestBody TbOrder tbOrder){
        tbOrderServiceImpl.saveOrUpdateRole(tbOrder);
        return "success";
    }

}
