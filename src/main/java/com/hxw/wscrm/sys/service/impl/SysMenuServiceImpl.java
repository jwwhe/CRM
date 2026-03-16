package com.hxw.wscrm.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hxw.wscrm.common.annotaion.SystemLog;
import com.hxw.wscrm.common.util.PageUtils;
import com.hxw.wscrm.sys.entity.SysMenu;
import com.hxw.wscrm.sys.entity.SysUser;
import com.hxw.wscrm.sys.mapper.SysMenuMapper;
import com.hxw.wscrm.sys.model.ShowMenu;
import com.hxw.wscrm.sys.model.SysMenuQueryDTO;
import com.hxw.wscrm.sys.service.ISysMenuService;
import com.hxw.wscrm.sys.service.ISysPermissionService;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单管理 服务实现类
 * </p>
 *
 * @author 小贺
 * @since 2025-08-09
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;
    
    @Autowired
    private ISysPermissionService sysPermissionService;
    
    @Autowired
    private com.hxw.wscrm.sys.service.ISysUserService sysUserService;

    /**
     * 查询所有的菜单信息
     * @param dto
     * @return
     */
    @Override
    public PageUtils listPage(SysMenuQueryDTO dto) {
        //先查询所有的一级菜单的数据，分页是针对一级菜单的数据分页
        QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",0)//查询所有的一级菜单
                .like(StringUtils.isNotBlank(dto.getLabel()),"label",dto.getLabel())
                .orderByAsc("order_num");
        Page<SysMenu> page = this.page(dto.page(), wrapper);
        // 查询出该一级菜单对应的二级菜单
        List<SysMenu> records = page.getRecords();
        List<SysMenu> collect = records.stream().map(item -> {
            //有子菜单
            Long menuId = item.getMenuId();
            //判断当前菜单是否可以被删除
            int count = sysMenuMapper.canBeDeleted(menuId);

            if (count == 0){
                item.setCanBeDeleted(true);
            }else {
                item.setCanBeDeleted(false);
            }
            //根据menuID查询出所有的二级菜单
            QueryWrapper<SysMenu> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("parent_id",menuId).orderByAsc("order_num");
            List<SysMenu> subMenus = this.baseMapper.selectList(wrapper1);
            for (SysMenu subMenu : subMenus) {
                //主要是判断子菜单是否被分配
                int i = sysMenuMapper.canBeDeleted(subMenu.getMenuId());
                if (i == 0){
                    subMenu.setCanBeDeleted(true);
                }else{
                    subMenu.setCanBeDeleted(false);
                }
            }
            item.setChildren(subMenus);
            return item;
        }).collect(Collectors.toList());
        page.setRecords(collect);
        return new PageUtils(page);
    }

    @Override
    public List<SysMenu> listParent() {
        List<SysMenu> list = this.baseMapper.selectList(new QueryWrapper<SysMenu>().eq("parent_id", 0));
        return list;
    }

    @SystemLog("菜单添加或者更新")
    @Override
    public void saveOrUpdateMenu(SysMenu menu) {

        if (menu != null && menu.getMenuId() > 0){
            //表示是更新操作
            this.updateById(menu);
        }else {
            if (StringUtils.isNotBlank(menu.getLabel()) && StringUtils.isBlank(menu.getName())){
                menu.setName(menu.getLabel());
            }
            //新增操作
            if (menu.getParentId() == null){
                menu.setParentId(0L);
            }
            this.save(menu);
            
            // 为新增的菜单生成增删改查四个权限
            // 使用菜单名称作为权限编码前缀
            String menuCode = menu.getName() != null ? menu.getName() : menu.getLabel();
            if (StringUtils.isNotBlank(menuCode)) {
                Long userId = getCurrentUserId();
                sysPermissionService.batchCreatePermissions(menu.getMenuId(), menuCode, userId);
            }
        }
    }

    @Override
    public SysMenu queryMenuById(Long menuId) {
        return this.baseMapper.selectById(menuId);
    }

    @SystemLog("菜单删除")
    @Override
    public String deleteMenuById(Long menuId) {
        //1.判断数据是否能够删除
        //2.如果能够删除就删除
       int count =  sysMenuMapper.canBeDeleted(menuId);
       if (count == 0){
           //表示数据可以删除
           // 先删除该菜单下的所有权限
           sysPermissionService.deletePermissionsByMenuId(menuId);
           // 再删除菜单
           this.baseMapper.deleteById(menuId);
           return "1";
       }
       //表示数据不能被删除
        return "0";
    }

    @Override
    public List<Integer> queryMenuIdByRoleId(Long roleId) {

        return sysMenuMapper.queryMenuIdByRoleId(roleId);
    }

    @Override
    public List<ShowMenu> getShowMenu() {
        //获取当前登录的用户
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String tempUserName = null;
        Object principal = token.getPrincipal();
        if (principal instanceof String) {
            tempUserName = (String) principal;
        } else if (principal instanceof SysUser) {
            // 如果principal是SysUser对象，直接调用getUsername()方法
            tempUserName = ((SysUser) principal).getUsername();
        } else if (principal instanceof org.springframework.security.core.userdetails.User) {
            // 如果principal是User对象，直接调用getUsername()方法
            tempUserName = ((org.springframework.security.core.userdetails.User) principal).getUsername();
        } else if (principal != null) {
            // 尝试从其他对象中获取用户名
            try {
                tempUserName = (String) principal.getClass().getMethod("getUsername").invoke(principal);
            } catch (Exception e) {
                // 忽略异常，userName保持为null
                System.err.println("Error getting username from principal: " + e.getMessage());
            }
        }
        // 如果没有获取到用户名，返回空列表
        if (tempUserName == null) {
            System.err.println("No username found in principal");
            return new ArrayList<>();
        }
        // 创建一个final的副本，用于lambda表达式中
        final String userName = tempUserName;
        System.out.println("Getting menu for user: " + userName);
        //查询当前登录用户的所有的父菜单
       List<SysMenu> list =  this.baseMapper.selectShowMenuParent(userName);
       System.out.println("Parent menu list size: " + (list != null ? list.size() : 0));
       if (list != null && list.size() > 0){
           return list.stream().map(item->{
               ShowMenu showMenu = new ShowMenu();
               showMenu.setIcon(item.getIcon());
               showMenu.setPath(item.getPath());
               showMenu.setName(item.getName());
               showMenu.setLabel(item.getLabel());
               showMenu.setUrl(item.getUrl());
               //获取对应的子菜单
               List<SysMenu> sublist =  this.baseMapper.selectShowMenuSubByUserName(userName,item.getMenuId());
               if (sublist != null && sublist.size() > 0){
                   List<ShowMenu> showMenus = new ArrayList<>();
                   for (SysMenu sysMenu : sublist) {
                       ShowMenu subMenu = new ShowMenu();
                       subMenu.setIcon(sysMenu.getIcon());
                       subMenu.setPath(sysMenu.getPath());
                       subMenu.setName(sysMenu.getName());
                       subMenu.setLabel(sysMenu.getLabel());
                       subMenu.setUrl(sysMenu.getUrl());
                       showMenus.add(subMenu);
                   }
                   showMenu.setChildren(showMenus);
               } else {
                   // Ensure children is null if empty, so frontend filters correctly
                   showMenu.setChildren(null);
               }

               return showMenu;
           }).collect(Collectors.toList());
       }

        return new ArrayList<>();
    }

    @Override
    public List<String> queryPermsByUserName(String userName) {
        return sysMenuMapper.queryPermsByUserName(userName);
    }
    
    /**
     * 获取当前登录用户ID
     * @return 用户ID
     */
    private Long getCurrentUserId() {
        //获取当前登录的用户
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Object principal = token.getPrincipal();
        if (principal instanceof String) {
            // 如果principal是用户名，查询用户信息
            String userName = (String) principal;
            List<SysUser> list = sysUserService.queryByUserName(userName);
            if (list != null && list.size() == 1) {
                return list.get(0).getUserId();
            }
        } else if (principal != null) {
            // 尝试从SysUser对象中获取用户ID
            try {
                return (Long) principal.getClass().getMethod("getUserId").invoke(principal);
            } catch (Exception e) {
                // 忽略异常
            }
        }
        return null;
    }
}
