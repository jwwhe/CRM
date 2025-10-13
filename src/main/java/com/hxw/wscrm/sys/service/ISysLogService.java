package com.hxw.wscrm.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hxw.wscrm.common.util.PageUtils;
import com.hxw.wscrm.sys.entity.SysLog;
import com.hxw.wscrm.sys.model.SysLogQueryDTO;

/**
 * <p>
 * 系统日志 服务类
 * </p>
 *
 * @author 小贺
 * @since 2025-08-21
 */
public interface ISysLogService extends IService<SysLog> {
    PageUtils listPage(SysLogQueryDTO dto);
}
