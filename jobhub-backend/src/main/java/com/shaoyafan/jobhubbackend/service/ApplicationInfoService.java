package com.shaoyafan.jobhubbackend.service;

import com.shaoyafan.jobhubbackend.model.domain.ApplicationInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shaoyafan.jobhubbackend.model.dto.applicationInfo.ApplicationInfoAddRequest;
import com.shaoyafan.jobhubbackend.model.dto.applicationInfo.ApplicationInfoUpdateRequest;
import com.shaoyafan.jobhubbackend.model.dto.user.UserIdRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * 在线简历服务
 *
* @author SYF
* @description 针对表【application_info(在线简历表)】的数据库操作Service
* @createDate 2025-01-26 16:39:45
*/
public interface ApplicationInfoService extends IService<ApplicationInfo> {

    /**
     * 新增在线简历
     *
     * @param applicationInfoAddRequest
     * @param request
     * @return
     */
    Long addApplicationInfo(ApplicationInfoAddRequest applicationInfoAddRequest, HttpServletRequest request);

    /**
     * 更新在线简历
     *
     * @param applicationInfoUpdateRequest
     * @param request
     * @return
     */
    Boolean updateApplicationInfo(ApplicationInfoUpdateRequest applicationInfoUpdateRequest, HttpServletRequest request);

    /**
     * 查看自己在线简历
     *
     * @param request
     * @return
     */
    ApplicationInfo getOwnApplicationInfo(HttpServletRequest request);

    /**
     * 根据 userId 查看在线简历
     *
     * @param userId
     * @return
     */
    ApplicationInfo getApplicationInfoByUserId(Long userId);
}
