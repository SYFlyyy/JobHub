package com.shaoyafan.jobhubbackend.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shaoyafan.jobhubbackend.common.ErrorCode;
import com.shaoyafan.jobhubbackend.constant.CommonConstant;
import com.shaoyafan.jobhubbackend.constant.RoleConstant;
import com.shaoyafan.jobhubbackend.constant.StatusConstant;
import com.shaoyafan.jobhubbackend.exception.BusinessException;
import com.shaoyafan.jobhubbackend.model.domain.Resume;
import com.shaoyafan.jobhubbackend.model.domain.User;
import com.shaoyafan.jobhubbackend.model.dto.user.*;
import com.shaoyafan.jobhubbackend.model.vo.user.LoginUserVO;
import com.shaoyafan.jobhubbackend.model.vo.user.UserVO;
import com.shaoyafan.jobhubbackend.service.UserService;
import com.shaoyafan.jobhubbackend.mapper.UserMapper;
import com.shaoyafan.jobhubbackend.utils.EmailUtils;
import com.shaoyafan.jobhubbackend.utils.PhoneNumberUtils;
import com.shaoyafan.jobhubbackend.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    private static final String Avatar_FILE_PATH = "file/userAvatar/";

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
        String email = userUpdateMyRequest.getEmail();
        if (StringUtils.isNotBlank(email) && !EmailUtils.isValidEmail(email)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱格式错误");
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
    public String uploadAvatar(MultipartFile file, HttpServletRequest request) {
        User user = this.getLoginUser(request);
        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件不能为空");
            }
            // 检查文件类型
            String contentType = file.getContentType();
            if (contentType == null
                    || !(contentType.equals("image/jpeg")
                    || contentType.equals("image/png"))) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "只支持 JPG、PNG文件");
            }
            // 创建上传目录（如果不存在）
            File uploadDir = new File(Avatar_FILE_PATH);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            String fileName = user.getId() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(Avatar_FILE_PATH + fileName);
            // 用户是否已上传头像
            if (StringUtils.isBlank(user.getAvatar())) {
                // 用户首次上传头像
                Files.copy(file.getInputStream(), filePath);
                user.setAvatar(filePath.toString());
                boolean result = this.updateById(user);
                if (!result) {
                    throw new BusinessException(ErrorCode.OPERATION_ERROR, "上传头像失败");
                }
            } else {
                // 用户已上传头像，删除原文件后再上传
                Path oldFilePath = Paths.get(user.getAvatar());
                if (Files.exists(oldFilePath)) {
                    try {
                        Files.delete(oldFilePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new BusinessException(ErrorCode.OPERATION_ERROR, "删除原文件失败");
                    }
                }
                Files.copy(file.getInputStream(), filePath);
                user.setAvatar(filePath.toString());
                boolean result = this.updateById(user);
                if (!result) {
                    throw new BusinessException(ErrorCode.OPERATION_ERROR, "上传头像失败");
                }
            }
            return user.getAvatar();
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "上传头像失败");
        }
    }

    @Override
    public Boolean updatePassword(UserUpdatePwdRequest userUpdatePwdRequest, HttpServletRequest request) {
        String oldPassword = userUpdatePwdRequest.getOldPassword();
        // 旧密码加密
        String oldEncryptPassword = DigestUtils.md5DigestAsHex((SALT + oldPassword).getBytes());
        User loginUser = getLoginUser(request);
        if (!Objects.equals(loginUser.getPassword(), oldEncryptPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "原密码错误");
        }
        String newPassword = userUpdatePwdRequest.getNewPassword();
        String checkPassword = userUpdatePwdRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(newPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "新密码或确认密码为空");
        }
        if (newPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "新密码过短");
        }
        if (!Objects.equals(newPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        // 新密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + newPassword).getBytes());
        loginUser.setPassword(encryptPassword);
        boolean result = this.updateById(loginUser);
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
        queryWrapper.like(StringUtils.isNotBlank(account), "account", account);
        queryWrapper.like(StringUtils.isNotBlank(username), "username", username);
        queryWrapper.like(StringUtils.isNotBlank(email), "email", email);
        queryWrapper.like(StringUtils.isNotBlank(phone), "phone", phone);
        queryWrapper.eq(role != null, "role", role);
        queryWrapper.eq(status != null, "status", status);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }

}




