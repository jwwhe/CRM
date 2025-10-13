package com.hxw.wscrm.sys.service.impl;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxw.wscrm.common.util.PageUtils;
import com.hxw.wscrm.sys.entity.SysLog;
import com.hxw.wscrm.sys.mapper.SysLogMapper;
import com.hxw.wscrm.sys.model.SysLogQueryDTO;
import com.hxw.wscrm.sys.service.ISysLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;



/**
 * <p>
 * 系统日志 服务实现类
 * </p>
 *
 * @author 小贺
 * @since 2025-08-21
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {
    /**
     * 查询系统日志信息
     *      用户名或者系统操作
     * @param dto
     * @return
     */
    @Override
    public PageUtils listPage(SysLogQueryDTO dto) {
        QueryWrapper<SysLog> wrapper = new QueryWrapper<SysLog>();
        // 只有当msg不为空时才添加查询条件
        if (StringUtils.isNotEmpty(dto.getMsg())) {
            wrapper.like("username", dto.getMsg())
                    .or()
                    .like("operation", dto.getMsg());
        }
        Page<SysLog> page = this.page(dto.page(), wrapper);
        return new PageUtils(page);
    }
}
