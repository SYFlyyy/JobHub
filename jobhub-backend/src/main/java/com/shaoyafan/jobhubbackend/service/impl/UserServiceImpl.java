package com.shaoyafan.jobhubbackend.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shaoyafan.jobhubbackend.common.ErrorCode;
import com.shaoyafan.jobhubbackend.constant.CommonConstant;
import com.shaoyafan.jobhubbackend.constant.RoleConstant;
import com.shaoyafan.jobhubbackend.constant.StatusConstant;
import com.shaoyafan.jobhubbackend.exception.BusinessException;
import com.shaoyafan.jobhubbackend.model.domain.User;
import com.shaoyafan.jobhubbackend.model.dto.user.UserLoginRequest;
import com.shaoyafan.jobhubbackend.model.dto.user.UserQueryRequest;
import com.shaoyafan.jobhubbackend.model.dto.user.UserRegisterRequest;
import com.shaoyafan.jobhubbackend.model.dto.user.UserUpdateMyRequest;
import com.shaoyafan.jobhubbackend.model.vo.LoginUserVO;
import com.shaoyafan.jobhubbackend.model.vo.UserVO;
import com.shaoyafan.jobhubbackend.service.UserService;
import com.shaoyafan.jobhubbackend.mapper.UserMapper;
import com.shaoyafan.jobhubbackend.utils.PhoneNumberUtils;
import com.shaoyafan.jobhubbackend.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
* @author SYF
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2025-01-26 16:39:45
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    /**
     * 盐值，混淆密码
     */
    public static final String SALT = "shaoyafan";

    @Override
    public Long userRegister(UserRegisterRequest userRegisterRequest) {
        String account = userRegisterRequest.getAccount();
        String password = userRegisterRequest.getPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        Integer role = userRegisterRequest.getRole();
        // 1. 校验
        if (StringUtils.isAnyBlank(account, password, checkPassword, role != null ? role.toString() : "")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (account.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (password.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        // 密码和校验密码相同
        if (!password.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        synchronized (account.intern()) {
            // 账户不能重复
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("account", account);
            long count = this.baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
            }
            // 2. 加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
            // 3. 插入数据
            User user = new User();
            user.setAccount(account);
            user.setPassword(encryptPassword);
            if (RoleConstant.CANDIDATE_ROLE.equals(role)) {
                // 求职者注册默认状态为正常
                user.setStatus(StatusConstant.NORMAL);
            } else {
                // 招聘者注册默认状态为待审核
                user.setStatus(StatusConstant.PENDING);
            }
            user.setRole(role);
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            return user.getId();
        }
    }

    @Override
    public LoginUserVO userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request) {
        String account = userLoginRequest.getAccount();
        String password = userLoginRequest.getPassword();
        // 1. 校验
        if (StringUtils.isAnyBlank(account, password)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (account.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (password.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account);
        queryWrapper.eq("password", encryptPassword);
        User user = this.baseMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("用户登录失败，账号或密码错误");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号或密码错误");
        }
        if (Objects.equals(user.getStatus(), StatusConstant.PENDING)) {
            throw new BusinessException(ErrorCode.STATE_ERROR, "用户待审核中");
        }
        if (Objects.equals(user.getStatus(), StatusConstant.DISABLED)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "用户已被禁用");
        }
        // 3. 记录用户的登录态
        request.getSession().setAttribute(StatusConstant.USER_LOGIN_STATE, user);
        return this.getLoginUserVO(user);
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(StatusConstant.USER_LOGIN_STATE);
        User user = (User) userObj;
        if (user == null || user.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        Long userId = user.getId();
        user = this.getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return user;
    }

    @Override
    public Boolean userLogout(HttpServletRequest request) {
        if (request.getSession().getAttribute(StatusConstant.USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "未登录");
        }
        // 移除登录态
        request.getSession().removeAttribute(StatusConstant.USER_LOGIN_STATE);
        return true;
    }

    @Override
    public Boolean updateMyUser(UserUpdateMyRequest userUpdateMyRequest, HttpServletRequest request) {
        User loginUser = this.getLoginUser(request);
        String phone = userUpdateMyRequest.getPhone();
        if (StringUtils.isNotBlank(phone) && !PhoneNumberUtils.isValidChinesePhoneNumber(phone)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "手机号格式错误");
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateMyRequest, user);
        user.setId(loginUser.getId());
        boolean result = this.updateById(user);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return true;
    }

    @Override
    public Boolean reviewUser(Long id) {
        User user = this.getById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }
        if (Objects.equals(user.getStatus(), StatusConstant.NORMAL)) {
            throw new BusinessException(ErrorCode.STATE_ERROR, "用户已审核");
        }
        // 审核通过
        user.setStatus(StatusConstant.NORMAL);
        boolean result = this.updateById(user);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return true;
    }

    @Override
    public Boolean freezeUser(Long id) {
        User user = this.getById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }
        if (Objects.equals(user.getStatus(), StatusConstant.DISABLED)) {
            throw new BusinessException(ErrorCode.STATE_ERROR, "用户已冻结");
        }
        // 冻结用户
        user.setStatus(StatusConstant.DISABLED);
        boolean result = this.updateById(user);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return true;
    }

    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public List<UserVO> getUserVO(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String account = userQueryRequest.getAccount();
        String username = userQueryRequest.getUsername();
        String email = userQueryRequest.getEmail();
        String phone = userQueryRequest.getPhone();
        Integer role = userQueryRequest.getRole();
        Integer status = userQueryRequest.getStatus();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(account), "account", account);
        queryWrapper.eq(StringUtils.isNotBlank(username), "username", username);
        queryWrapper.eq(StringUtils.isNotBlank(email), "email", email);
        queryWrapper.eq(StringUtils.isNotBlank(phone), "phone", phone);
        queryWrapper.eq(role != null, "role", role);
        queryWrapper.eq(status != null, "status", status);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }

}




