package com.hxw.wscrm.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hxw.wscrm.common.util.PageUtils;
import com.hxw.wscrm.sys.entity.SysMenu;
import com.hxw.wscrm.sys.model.ShowMenu;
import com.hxw.wscrm.sys.model.SysMenuQueryDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单管理 服务类
 * </p>
 *
 * @author 小贺
 * @since 2025-08-09
 */
public interface ISysMenuService extends IService<SysMenu> {

    PageUtils listPage(SysMenuQueryDTO dto);

    List<SysMenu> listParent();

    void saveOrUpdateMenu(SysMenu menu);

    SysMenu queryMenuById(Long menuId);

    String deleteMenuById(Long menuId);

    List<Integer> queryMenuIdByRoleId(Long roleId);

    List<ShowMenu> getShowMenu();

    List<String> queryPermsByUserName(String userName);
}
