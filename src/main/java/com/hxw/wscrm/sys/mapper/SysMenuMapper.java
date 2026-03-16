package com.hxw.wscrm.sys.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hxw.wscrm.sys.entity.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单管理 Mapper 接口
 * </p>
 *
 * @author 小贺
 * @since 2025-08-09
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    int canBeDeleted(@Param("menuId") Long menuId);

    List<Integer> queryMenuIdByRoleId(@Param("roleId") Long roleId);

    List<SysMenu> selectShowMenuParent(@Param("userName") String userName);

    List<SysMenu> selectShowMenuSubByUserName(@Param("userName") String userName, @Param("menuId") Long menuId);

    List<String> queryPermsByUserName(@Param("userName") String userName);
}
