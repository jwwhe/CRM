package com.hxw.wscrm.sys.controller;

import com.hxw.wscrm.common.util.PageUtils;
import com.hxw.wscrm.sys.entity.SysLog;
import com.hxw.wscrm.sys.model.SysLogQueryDTO;
import com.hxw.wscrm.sys.service.ISysLogService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 系统日志 前端控制器
 * </p>
 *
 * @author 小贺
 * @since 2025-08-21
 */
@RestController
@RequestMapping("/sys/sysLog")
@Api(tags = "系统日志",value = "SysLog")
public class SysLogController {
    @Autowired
    ISysLogService sysLogService;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:query')")
    public PageUtils list(SysLogQueryDTO dto){
       PageUtils pageUtils =  sysLogService.listPage(dto);
        return  pageUtils;
    }
}
