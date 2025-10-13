package com.hxw.wscrm.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hxw.wscrm.common.annotaion.SystemLog;
import com.hxw.wscrm.common.util.PageUtils;
import com.hxw.wscrm.sys.entity.SysMenu;
import com.hxw.wscrm.sys.entity.SysRole;
import com.hxw.wscrm.sys.mapper.SysRoleMapper;
import com.hxw.wscrm.sys.model.SysRoleQueryDTO;
import com.hxw.wscrm.sys.service.ISysRoleService;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author 小贺
 * @since 2025-08-09
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysMenuServiceImpl menuService;

    @Override
    public PageUtils queryPage(SysRoleQueryDTO queryDTO) {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<SysRole>().like(StringUtils.isNotEmpty(queryDTO.getRoleName()),"role_name",queryDTO.getRoleName());
        Page<SysRole> page = this.page(queryDTO.page(),wrapper);
        return new PageUtils(page);
    }

    @SystemLog("新增或修改角色")
    @Override
    @Transactional
    public void saveOrUpdateRole(SysRole role) {
        //同步维护角色和菜单的关系
        if (role.getRoleId() != null){
            this.update(role);
            // 根据roleID 删除分配的菜单信息
            sysRoleMapper.deleteMenuByRoleId(role.getRoleId());
        }else {
           this.saveRole(role);
        }
        //  新增分配的菜单信息
        List<Integer> menuIds = role.getMenuIds();
        if (menuIds != null && menuIds.size() > 0){
            // 说明分配的有相关的信息
            for (Integer menuId : menuIds) {
                sysRoleMapper.insertRoleAndMenu(role.getRoleId(),menuId);
            }
        }
    }
    @Override
    public void update(SysRole role) {
            this.baseMapper.updateById(role);
    }

    @Override
    public void deleteBatch(Long[] roleIds) {

    }

    /**
     * @param roleName 传入角色名称
     * @return 返回布尔值 ture 存在 false 不存在
     */
    @Override
    public boolean checkRoleName(String roleName,Long roleId) {
        if (StringUtils.isBlank(roleName)) {
            return false;
        }
        QueryWrapper<SysRole> wrapper = new QueryWrapper<SysRole>().eq("role_name",roleName);
        if (roleId != null) {
            wrapper.ne("role_id", roleId); // 添加条件：角色ID不等于当前ID
        }
        int count = this.count(wrapper);
        return count > 0 ;
    }
    @SystemLog("删除角色")
    @Override
    public boolean deleteRoleById(Long roleId) {
        //删除角色信息
        //如果这个角色分配给了用户或者角色绑定了菜单那么不允许删除角色
        //查看该角色是否分配给了用户
        int count = this.baseMapper.checkRoleCanDelete(roleId);
        if (count == 0){
            this.baseMapper.deleteById(roleId);
        }
        return count == 0;
    }

    @Override
    public List<SysRole> queryByUserId(Long userId) {
      return   sysRoleMapper.queryByUserId(userId);

    }

    /**
     * 查询角色分配的菜单信息
     * @param roleId
     * @return
     */
    @Override
    public Map<String, Object> dispatherRoleMenu(Long roleId) {
        List<SysMenu> parents = menuService.listParent();
        List<Map<String,Object>> list = new ArrayList<>();
        if (parents != null && parents.size() > 0) {
            for (SysMenu parent : parents) {
                Map<String,Object> map = new HashMap<>();
                map.put("id",parent.getMenuId());
                map.put("label",parent.getLabel());
                //根据父菜单编号查询对应的子菜单信息
                Long parentId = parent.getMenuId();
                QueryWrapper<SysMenu> wrapper = new QueryWrapper<SysMenu>().eq("parent_id",parentId);
                List<SysMenu> subMenus = menuService.list(wrapper);
                List<Map<String,Object>> subList = new ArrayList<>();
                if (subMenus != null && subMenus.size() > 0) {
                    for (SysMenu subMenu : subMenus) {
                        Map<String,Object> subMap = new HashMap<>();
                        subMap.put("id",subMenu.getMenuId());
                        subMap.put("label",subMenu.getName());
                        subList.add(subMap);
                    }
                }
                //父子菜单关联
                map.put("children",subList);
                list.add(map);
            }
        }
        //根据角色编号查询分配的菜单编号
      List<Integer> menudIds =   menuService.queryMenuIdByRoleId(roleId);
        Map<String,Object> resMap = new HashMap<>();
        resMap.put("checks",menudIds);
        resMap.put("treeData",list);
        return resMap;
    }

    public void saveRole(SysRole sysRole){
        sysRole.setCreateTime(LocalDateTime.now());
        this.save(sysRole);
    }
}
