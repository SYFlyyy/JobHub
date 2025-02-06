package com.shaoyafan.jobhubbackend.aop;

import com.shaoyafan.jobhubbackend.common.ErrorCode;
import com.shaoyafan.jobhubbackend.constant.StatusConstant;
import com.shaoyafan.jobhubbackend.exception.BusinessException;
import com.shaoyafan.jobhubbackend.model.domain.User;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录校验 AOP
 *
 * @author SYF
 */
@Aspect
@Component
public class AuthAspect {

    @Autowired
    private HttpServletRequest request;

    @Before("execution(* com.shaoyafan.jobhubbackend.controller.*.*(..)) && " +
            "!execution(* com.shaoyafan.jobhubbackend.controller.UserController.userLogin(..)) && " +
            "!execution(* com.shaoyafan.jobhubbackend.controller.UserController.userRegister(..))")
    public void checkLogin() {
        Object userObj = request.getSession().getAttribute(StatusConstant.USER_LOGIN_STATE);
        User user = (User) userObj;
        if (user == null || user.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
    }
}
