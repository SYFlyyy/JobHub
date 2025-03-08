package com.shaoyafan.jobhubbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shaoyafan.jobhubbackend.common.ErrorCode;
import com.shaoyafan.jobhubbackend.exception.BusinessException;
import com.shaoyafan.jobhubbackend.model.domain.ApplicationInfo;
import com.shaoyafan.jobhubbackend.model.domain.User;
import com.shaoyafan.jobhubbackend.model.dto.applicationInfo.ApplicationInfoAddRequest;
import com.shaoyafan.jobhubbackend.model.dto.applicationInfo.ApplicationInfoUpdateRequest;
import com.shaoyafan.jobhubbackend.model.dto.user.UserIdRequest;
import com.shaoyafan.jobhubbackend.service.ApplicationInfoService;
import com.shaoyafan.jobhubbackend.mapper.ApplicationInfoMapper;
import com.shaoyafan.jobhubbackend.service.UserService;
import com.shaoyafan.jobhubbackend.utils.EmailUtils;
import com.shaoyafan.jobhubbackend.utils.PhoneNumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
* @author SYF
* @description 针对表【application_info(在线简历表)】的数据库操作Service实现
* @createDate 2025-01-26 16:39:45
*/
@Service
public class ApplicationInfoServiceImpl extends ServiceImpl<ApplicationInfoMapper, ApplicationInfo>
    implements ApplicationInfoService {

    @Resource
    private UserService userService;

    @Override
    @Transactional
    public Long addApplicationInfo(ApplicationInfoAddRequest applicationInfoAddRequest, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        // 判断当前用户是否已存在在线简历
        ApplicationInfo existApplicationInfo = this.getOne(new QueryWrapper<ApplicationInfo>().eq("user_id", userId));
        if (existApplicationInfo != null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "当前用户已存在在线简历");
        }
        String name = applicationInfoAddRequest.getName();
        if (StringUtils.isBlank(name)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "姓名不能为空");
        }
        // 更新用户姓名
        loginUser.setUsername(name);
        userService.updateById(loginUser);
        String phone = applicationInfoAddRequest.getPhone();
        if (StringUtils.isNotBlank(phone) && !PhoneNumberUtils.isValidChinesePhoneNumber(phone)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "手机号格式错误");
        }
        String email = applicationInfoAddRequest.getEmail();
        if (StringUtils.isNotBlank(email) && !EmailUtils.isValidEmail(email)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱格式错误");
        }
        ApplicationInfo applicationInfo = new ApplicationInfo();
        BeanUtils.copyProperties(applicationInfoAddRequest, applicationInfo);
        applicationInfo.setUserId(userId);
        boolean result = this.save(applicationInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "新增在线简历失败");
        }
        return applicationInfo.getId();
    }

    @Override
    @Transactional
    public Boolean updateApplicationInfo(ApplicationInfoUpdateRequest applicationInfoUpdateRequest, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        // 判断当前用户是否已存在在线简历
        ApplicationInfo applicationInfo = this.getOne(new QueryWrapper<ApplicationInfo>().eq("user_id", userId));
        if (applicationInfo == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "当前用户没有在线简历");
        }
        String name = applicationInfoUpdateRequest.getName();
        if (StringUtils.isBlank(name)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "姓名不能为空");
        }
        // 更新用户姓名
        loginUser.setUsername(name);
        userService.updateById(loginUser);
        String phone = applicationInfoUpdateRequest.getPhone();
        if (StringUtils.isNotBlank(phone) && !PhoneNumberUtils.isValidChinesePhoneNumber(phone)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "手机号格式错误");
        }
        String email = applicationInfoUpdateRequest.getEmail();
        if (StringUtils.isNotBlank(email) && !EmailUtils.isValidEmail(email)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱格式错误");
        }
        BeanUtils.copyProperties(applicationInfoUpdateRequest, applicationInfo);
        boolean result = this.updateById(applicationInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "新增在线简历失败");
        }
        return true;
    }

    @Override
    public ApplicationInfo getOwnApplicationInfo(HttpServletRequest request) {
        Long userId = userService.getLoginUser(request).getId();
        ApplicationInfo applicationInfo = this.getOne(new QueryWrapper<ApplicationInfo>().eq("user_id", userId));
        if (applicationInfo == null) {
            return null;
        }
        return applicationInfo;
    }

    @Override
    public ApplicationInfo getApplicationInfoByUserId(Long userId) {
        if (userId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ApplicationInfo applicationInfo = this.getOne(new QueryWrapper<ApplicationInfo>().eq("user_id", userId));
        // if (applicationInfo == null) {
        //     throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户暂无在线简历");
        // }
        return applicationInfo;
    }
}




