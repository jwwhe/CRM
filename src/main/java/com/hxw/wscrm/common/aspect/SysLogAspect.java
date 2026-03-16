package com.hxw.wscrm.common.aspect;

import com.alibaba.fastjson.JSON;
import com.hxw.wscrm.common.annotaion.SystemLog;
import com.hxw.wscrm.sys.entity.SysLog;
import com.hxw.wscrm.sys.service.ISysLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Date;

@Component
@Aspect
@EnableAspectJAutoProxy
public class SysLogAspect {

    @Autowired
    ISysLogService sysLogService;

    @Pointcut("@annotation(com.hxw.wscrm.common.annotaion.SystemLog)")
    public void logPoint(){

    }

    @Around("logPoint()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        //执行开始的时间
        long begin = System.currentTimeMillis();
        Object obj = proceedingJoinPoint.proceed();
        //获取到 目标方法执行的时长
        long time = System.currentTimeMillis() - begin;
        //保存日志操作信息
        saveSysLog(proceedingJoinPoint,time);
        return obj;
    }

    /**
     * 保存日志相关的方法
     *      操作的用户
     *      具体的操作
     *      操作的方法名称
     *      参数列表
     *      执行的时长
     *      客户端的ip
     *      记录创建的时间
     * @param proceedingJoinPoint
     */
    private void saveSysLog(ProceedingJoinPoint proceedingJoinPoint,long time) {
        SysLog sysLog = new SysLog();
        //获取当前调用的方法
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        //需要获取 方法@SystemLog注解的描述信息
        SystemLog annotation = method.getAnnotation(SystemLog.class);
        if (annotation != null) {
            //绑定operation的值
            sysLog.setOperation(annotation.value());
        }
        //绑定方法的名称
        String className = proceedingJoinPoint.getTarget().getClass().getName();
        String methodName = method.getName();
        sysLog.setMethod(className+"."+methodName+"()");
        //获取方法的参数列表
        Object[] args = proceedingJoinPoint.getArgs();
        String params = JSON.toJSON(args).toString();
        sysLog.setParams(params);
        //方法调用的执行时长
        sysLog.setTime(time);
        //记录创建时间
        sysLog.setCreateDate(LocalDateTime.now());
        //记录操作人
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String userName = null;
        Object principal = token.getPrincipal();
        if (principal instanceof String) {
            userName = (String) principal;
        } else if (principal != null) {
            // 尝试从SysUser对象中获取用户名
            try {
                userName = (String) principal.getClass().getMethod("getUsername").invoke(principal);
            } catch (Exception e) {
                // 忽略异常，userName保持为null
            }
        }
        sysLog.setUsername(userName);
        sysLogService.save(sysLog);
    }
}
