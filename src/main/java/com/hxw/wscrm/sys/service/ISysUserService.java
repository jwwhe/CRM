package com.hxw.wscrm.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hxw.wscrm.common.util.PageUtils;
import com.hxw.wscrm.sys.entity.SysUser;
import com.hxw.wscrm.sys.model.SysUserQueryDTO;

import java.util.List;

/**
 * <p>
 * 系统用户 服务类
 * </p>
 *
 * @author 小贺
 * @since 2025-08-09
 */
public interface ISysUserService extends IService<SysUser> {

    List<SysUser> queryByUserName(String username);

    PageUtils queryPage(SysUserQueryDTO dto);

    boolean checkUserName(String username);

    void saveOrUpdateUser(SysUser user);

    SysUser queryByUserId(Long userId);
}
