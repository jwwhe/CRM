package com.hxw.wscrm.sys.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hxw.wscrm.sys.entity.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
}
