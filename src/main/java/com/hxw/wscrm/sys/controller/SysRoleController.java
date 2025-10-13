package com.hxw.wscrm.sys.controller;

import com.hxw.wscrm.common.constant.SystemConstant;
import com.hxw.wscrm.common.util.PageUtils;
import com.hxw.wscrm.sys.entity.SysRole;
import com.hxw.wscrm.sys.model.SysRoleQueryDTO;
import com.hxw.wscrm.sys.service.ISysRoleService;
import com.hxw.wscrm.sys.service.impl.SysRoleServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色 前端控制器
 * </p>
 *
 * @author 小贺
 * @since 2025-08-09
 */
@Api(tags = "系统角色",value = "SysRole")
@Controller
@RequestMapping("/sys/sysRole")
public class SysRoleController {

    @Autowired
    private ISysRoleService roleService;

    @ApiOperation(value = "查询分页角色",notes = "角色信息")
    @GetMapping("/list")
    public PageUtils list(@ApiParam(value = "查询条件")SysRoleQueryDTO queryDTO) {
        return roleService.queryPage(queryDTO);
    }
    @ApiOperation(value = "添加角色",notes = "添加角色信息")
    @PostMapping("/saveorupdate")
    public String save(@RequestBody SysRole sysRole){
        roleService.saveOrUpdateRole(sysRole);
        return "success";
    }
    @ApiOperation(value = "检查角色名称是否存在",notes = "校验角色名称")
    @GetMapping("/checkRoleName")
    public String checkRoleName(String roleName,Long roleId){
        boolean flag = roleService.checkRoleName(roleName,roleId);
        return flag ? SystemConstant.CHECK_SUCCESS : SystemConstant.CHECK_FAIL;
    }

    @ApiOperation(value = "删除角色",notes = "删除角色信息")
    @GetMapping("/delete")
    public String deleteRole(Long roleId){
        boolean flag = this.roleService.deleteRoleById(roleId);
        return  flag ? "1" :"0";
    }

    @ApiOperation(value = "查询分配的菜单信息",notes = "查询分配的菜单信息")
    @GetMapping("/dispatherRoleMenu")
    public Map<String,Object> dispatherRoleMenu(Long roleId){
         return    roleService.dispatherRoleMenu(roleId);
    }
    @ApiOperation(value = "查询所有角色信息")
    @GetMapping("/listAll")
    public List<SysRole> listAll() {
        return roleService.list();
    }


}
