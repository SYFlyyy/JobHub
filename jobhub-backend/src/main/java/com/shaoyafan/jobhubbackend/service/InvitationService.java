package com.shaoyafan.jobhubbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shaoyafan.jobhubbackend.model.domain.Invitation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shaoyafan.jobhubbackend.model.dto.invitation.InvitationAddRequest;
import com.shaoyafan.jobhubbackend.model.dto.invitation.InvitationIdRequest;
import com.shaoyafan.jobhubbackend.model.dto.invitation.InvitationQueryRequest;

import javax.servlet.http.HttpServletRequest;

/**
* @author SYF
* @description 针对表【invitation(面试邀请表)】的数据库操作Service
* @createDate 2025-03-09 12:33:26
*/
public interface InvitationService extends IService<Invitation> {

    /**
     * 新增面试邀请
     *
     * @param invitationAddRequest
     * @param request
     * @return
     */
    Boolean addInvitation(InvitationAddRequest invitationAddRequest, HttpServletRequest request);

    /**
     * 接受面试邀请
     *
     * @param invitationIdRequest
     * @return
     */
    Boolean acceptInvitation(InvitationIdRequest invitationIdRequest, HttpServletRequest request);

    /**
     * 拒绝面试邀请
     *
     * @param invitationIdRequest
     * @return
     */
    Boolean rejectInvitation(InvitationIdRequest invitationIdRequest, HttpServletRequest request);

    /**
     * 构建查询条件
     *
     * @param invitationQueryRequest
     * @return
     */
    QueryWrapper<Invitation> getInvitationQueryWrapper(InvitationQueryRequest invitationQueryRequest);

}
