package com.hxw.wscrm.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hxw.wscrm.common.annotaion.SystemLog;
import com.hxw.wscrm.common.constant.SystemConstant;
import com.hxw.wscrm.common.util.PageUtils;
import com.hxw.wscrm.sys.entity.SysUser;
import com.hxw.wscrm.sys.mapper.SysUserMapper;
import com.hxw.wscrm.sys.model.SysUserQueryDTO;
import com.hxw.wscrm.sys.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 系统用户 服务实现类
 * </p>
 *
 * @author 小贺
 * @since 2025-08-09
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Override
    public List<SysUser> queryByUserName(String username) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotEmpty(username), "username", username);
        wrapper.eq("status", SystemConstant.USER_STATUS_NORMAL);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public PageUtils queryPage(SysUserQueryDTO dto) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(dto.getUsername()), "username", dto.getUsername());
        Page<SysUser> page = this.page(dto.page(), wrapper);
        return new PageUtils(page);
    }

    @Override
    public boolean checkUserName(String username) {
        List<SysUser> list = baseMapper.selectList(new QueryWrapper<SysUser>().eq("username", username));
        if (list != null && list.size() > 0) {
            //说明账号存在
            return true;
        }
        //说明账号不存在
        return false;
    }

    @Transactional
    @SystemLog("用户的添加或者更新")
    @Override
    public void saveOrUpdateUser(SysUser user) {
        // 1.完成用户更新和删除
        if (user.getUserId() > 0){
            //更新
            this.updateById(user);
        }else {
            //添加
            user.setCreateTime(LocalDateTime.now());
            user.setCreateUserId(this.getCurrentUserId());
            //密码需要做加密处理
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String password = bCryptPasswordEncoder.encode(user.getPassword());
            user.setPassword(password);
            this.save(user);

        }
        // 2.完成用户分配的角色的保存
        if (user.getUserId() > 0){
            // 删除当前更新用户分配的所有角色
            this.baseMapper.deleteRoleByUserId(user.getUserId());
        }
        if (user.getRoleIds() != null && user.getRoleIds().size() > 0){
            for (Integer roleId : user.getRoleIds()) {
                this.baseMapper.saveUserAndRole(user.getUserId(),roleId);
            }
        }
    }

    private  List<SysUser> queryUser(SysUser user) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(user.getUsername()), "username", user.getUsername())
                .eq(StringUtils.isNotBlank(user.getEmail()), "email", user.getEmail())
                .eq(StringUtils.isNotBlank(user.getMobile()), "mobile", user.getMobile())
                .eq(user.getUserId() != null && user.getUserId() > 0, "user_id", user.getUserId());
      return   this.baseMapper.selectList(wrapper);
    }
    @Override
    public SysUser queryByUserId(Long userId) {
        SysUser user = new SysUser();
        user.setUserId(userId);
        List<SysUser> list = this.queryUser(user);
        if (list != null && list.size() > 0) {
            SysUser sysUser = list.get(0);
            sysUser.setPassword(null);
            //根据当前用户查询出对应的角色信息
          List<Integer> roleIds =  this.baseMapper.selectRoleIdsByUserId(sysUser.getUserId());
          sysUser.setRoleIds(roleIds);
            return sysUser;
        }
        return null;
    }

    public Long getCurrentUserId() {
        //设置添加数据的账号
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String userName = (String) authentication.getPrincipal();
        List<SysUser> list = this.baseMapper.selectList(new QueryWrapper<SysUser>().eq("username", userName));
        if (list != null && list.size() == 1) {
            SysUser user = list.get(0);
            return user.getUserId();
        }
        return null;
    }
}
