package com.hxw.wscrm.sys.controller;

import com.hxw.wscrm.common.constant.SystemConstant;
import com.hxw.wscrm.common.util.PageUtils;
import com.hxw.wscrm.sys.entity.SysMenu;
import com.hxw.wscrm.sys.model.MenuUpdateDTO;
import com.hxw.wscrm.sys.model.ShowMenu;
import com.hxw.wscrm.sys.model.SysMenuQueryDTO;
import com.hxw.wscrm.sys.service.ISysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 菜单管理 前端控制器
 * </p>
 *
 * @author 小贺
 * @since 2025-08-09
 */
@Api(tags = "菜单",value = "SysMenu")
@Controller
@RequestMapping("/sys/sysMenu")
public class SysMenuController {
    @Autowired
    private ISysMenuService menuService;

    @ApiOperation(value = "查询菜单信息",notes = "查询菜单信息")
    @GetMapping("/list")
    public PageUtils list(SysMenuQueryDTO dto){

        return menuService.listPage(dto);
    }

    @GetMapping("/listParent")
    @ApiOperation(value = "查询所有父菜单信息",notes = "查询所有父菜单信息")
    public List<SysMenu> listParent(){
        return menuService.listParent();
    }

    @PostMapping("/save")
    @ApiOperation(value = "操作菜单数据",notes = "添加/更新菜单")
    public String save(@RequestBody SysMenu menu){
        if (menu != null){
            menuService.saveOrUpdateMenu(menu);
        }
        return SystemConstant.CHECK_SUCCESS;
    }

    @GetMapping("/queryMenuById")
    @ApiOperation(value = "根据ID查询菜单",notes = "根据ID查询菜单")
    public MenuUpdateDTO queryMenuById(Long menuId){
        SysMenu sysMenu = menuService.queryMenuById(menuId);
        List<SysMenu> parents = menuService.listParent();
        return new MenuUpdateDTO(parents,sysMenu);
    }

    @GetMapping("/deleteMenu")
    @ApiOperation(value = "删除菜单",notes = "删除菜单")
    public String deleteMenu(Long menuId){
        //不能删除返回0 否则返回1
    return   menuService.deleteMenuById(menuId);
    }

    @ApiOperation(value = "获取当前登录用户的菜单",notes = "获取当前登录用户的菜单")
    @GetMapping("/getShowMenu")
    public List<ShowMenu> getShowMenu(){
    return menuService.getShowMenu();
    }
}
