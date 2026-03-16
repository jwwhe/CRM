package com.hxw.wscrm.sys.controller;

import com.hxw.wscrm.common.constant.SystemConstant;
import com.hxw.wscrm.common.util.PageUtils;
import com.hxw.wscrm.sys.entity.SysRole;
import com.hxw.wscrm.sys.entity.SysUser;
import com.hxw.wscrm.sys.model.SysUserQueryDTO;
import com.hxw.wscrm.sys.service.ISysRoleService;
import com.hxw.wscrm.sys.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统用户 前端控制器
 * </p>
 *
 * @author 小贺
 * @since 2025-08-09
 */
@Api(tags = "系统用户信息",value = "SysUser")
@RestController
@RequestMapping("/sys/sysUser")
public class SysUserController {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;


    @ApiOperation(value = "查询系统用户",notes = "查询用户")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:query')")
    public PageUtils list(SysUserQueryDTO dto) {
        return userService.queryPage(dto);
    }

    @ApiOperation(value = "检查用户名是否存在",notes = "检查账号是否存在")
    @GetMapping("/checkUserName")
    @PreAuthorize("hasAuthority('sys:query')")
    public String checkUserName(String username){
        // flag == ture 账号存在不可以使用
       boolean flag =  userService.checkUserName(username);
        return flag ? SystemConstant.CHECK_SUCCESS : SystemConstant.CHECK_FAIL;
    }
    @ApiOperation(value = "添加或者更新用户",notes = "添加或者更新用户")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:add') or hasAuthority('sys:update')")
    public String save(@RequestBody SysUser user){
        userService.saveOrUpdateUser(user);
        return "success";
    }

    @ApiOperation(value = "根据iD查询",notes = "根据iD查询用户信息")
    @GetMapping("/queryUserById")
    @PreAuthorize("hasAuthority('sys:query')")
    public Map<String,Object> queryUserById(Long userId){
        //根据ID查询用户信息
        SysUser sysUser = userService.queryByUserId(userId);
        //查询所有的角色信息
        List<SysRole> roles = roleService.list();
        Map<String,Object> map = new HashMap<>();
        map.put("user",sysUser);
        map.put("roles",roles);
        return map;
    }

    @ApiOperation(value = "根据角色ID查询用户", notes = "查询拥有指定角色的所有用户")
    @GetMapping("/queryByRoleId")
    @PreAuthorize("hasAuthority('sys:query')")
    public List<SysUser> queryByRoleId(@RequestParam Integer roleId) {
        return userService.queryByRoleId(roleId);
    }
}
