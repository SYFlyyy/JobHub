package com.shaoyafan.jobhubbackend.aop;

import com.shaoyafan.jobhubbackend.annotation.AuthCheck;
import com.shaoyafan.jobhubbackend.common.ErrorCode;
import com.shaoyafan.jobhubbackend.exception.BusinessException;
import com.shaoyafan.jobhubbackend.model.domain.User;
import com.shaoyafan.jobhubbackend.model.enums.UserRoleEnum;
import com.shaoyafan.jobhubbackend.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 权限校验 AOP
 *
 * @author SYF
 */
@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;

    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        // 注解需要的权限
        Integer mustRole = authCheck.mustRole();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
        // 不需要权限直接放行
        if (mustRoleEnum == null) {
            return joinPoint.proceed();
        }
        // 获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        // 必须有该权限才通过
        UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(loginUser.getRole());
        if (userRoleEnum == null) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 必须要有管理员权限
        if (UserRoleEnum.ADMIN.equals(mustRoleEnum)) {
            if (!UserRoleEnum.ADMIN.equals(userRoleEnum)) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "没有管理员权限");
            }
        }
        // 必须要有招聘者权限
        if (UserRoleEnum.RECRUITER.equals(mustRoleEnum)) {
            if (!UserRoleEnum.RECRUITER.equals(userRoleEnum)) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "没有招聘者权限");
            }
        }
        // 必须要有求职者权限
        if (UserRoleEnum.CANDIDATE.equals(mustRoleEnum)) {
            if (!UserRoleEnum.CANDIDATE.equals(userRoleEnum)) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "没有求职者权限");
            }
        }
        // 权限通过
        return joinPoint.proceed();
    }
}
