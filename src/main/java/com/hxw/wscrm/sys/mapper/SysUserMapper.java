package com.hxw.wscrm.sys.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hxw.wscrm.sys.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统用户 Mapper 接口
 * </p>
 *
 * @author 小贺
 * @since 2025-08-09
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    void deleteRoleByUserId(@Param("userId") Long userId);

    void saveUserAndRole(@Param("userId") Long userId,@Param("roleId") Integer roleId);

    List<Integer> selectRoleIdsByUserId(@Param("userId") Long userId);
}
