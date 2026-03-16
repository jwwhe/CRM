package com.hxw.wscrm.sys.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hxw.wscrm.sys.entity.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色 Mapper 接口
 * </p>
 *
 * @author 小贺
 * @since 2025-08-09
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    int checkRoleCanDelete(@Param("roleId") Long roleId);

    List<SysRole> queryByUserId(Long userId);

    void deleteMenuByRoleId(@Param("roleId") Long roleId);

    void insertRoleAndMenu(@Param("roleId") Long roleId, @Param("menuId") Integer menuId);

    List<Map<String, Object>> queryPermissionsByMenuId(@Param("menuId") Long menuId);

    List<Long> queryPermissionIdByRoleId(@Param("roleId") Long roleId);

    void deletePermissionByRoleId(@Param("roleId") Long roleId);

    void insertRoleAndPermission(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);
}
