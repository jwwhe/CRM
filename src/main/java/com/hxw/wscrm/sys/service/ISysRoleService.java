package com.hxw.wscrm.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hxw.wscrm.common.util.PageUtils;
import com.hxw.wscrm.sys.entity.SysRole;
import com.hxw.wscrm.sys.model.SysRoleQueryDTO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色 服务类
 * </p>
 *
 * @author 小贺
 * @since 2025-08-09
 */
public interface ISysRoleService extends IService<SysRole> {
    PageUtils queryPage(SysRoleQueryDTO queryDTO);

    void saveOrUpdateRole(SysRole role);

    void update(SysRole role);

    void deleteBatch(Long[] roleIds);

    boolean checkRoleName(String roleName, Long userId);

    boolean deleteRoleById(Long roleId);

    List<SysRole> queryByUserId(Long userId);

    Map<String, Object> dispatherRoleMenu(Long roleId);
}

