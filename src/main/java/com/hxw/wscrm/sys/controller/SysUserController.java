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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

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
@Controller
@RequestMapping("/sys/sysUser")
public class SysUserController {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;


    @ApiOperation(value = "查询系统用户",notes = "查询用户")
    @GetMapping("/list")
    public PageUtils list(SysUserQueryDTO dto) {
        return userService.queryPage(dto);
    }

    @ApiOperation(value = "检查用户名是否存在",notes = "检查账号是否存在")
    @GetMapping("/checkUserName")
    public String checkUserName(String username){
        // flag == ture 账号存在不可以使用
       boolean flag =  userService.checkUserName(username);
        return flag ? SystemConstant.CHECK_SUCCESS : SystemConstant.CHECK_FAIL;
    }
    @ApiOperation(value = "添加或者更新用户",notes = "添加或者更新用户")
    @PostMapping("/save")
    public String save(@RequestBody SysUser user){
        userService.saveOrUpdateUser(user);
        return "success";
    }

    @ApiOperation(value = "根据iD查询",notes = "根据iD查询用户信息")
    @GetMapping("/queryUserById")
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
}
