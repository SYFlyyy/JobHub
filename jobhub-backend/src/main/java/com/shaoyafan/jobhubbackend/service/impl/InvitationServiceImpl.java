package com.shaoyafan.jobhubbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shaoyafan.jobhubbackend.common.ErrorCode;
import com.shaoyafan.jobhubbackend.constant.CommonConstant;
import com.shaoyafan.jobhubbackend.constant.HiringStatusConstant;
import com.shaoyafan.jobhubbackend.constant.InvitationStatus;
import com.shaoyafan.jobhubbackend.exception.BusinessException;
import com.shaoyafan.jobhubbackend.model.domain.*;
import com.shaoyafan.jobhubbackend.mapper.InvitationMapper;
import com.shaoyafan.jobhubbackend.model.dto.invitation.InvitationAddRequest;
import com.shaoyafan.jobhubbackend.model.dto.invitation.InvitationIdRequest;
import com.shaoyafan.jobhubbackend.model.dto.invitation.InvitationQueryRequest;
import com.shaoyafan.jobhubbackend.service.*;
import com.shaoyafan.jobhubbackend.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
* @author SYF
* @description 针对表【invitation(面试邀请表)】的数据库操作Service实现
* @createDate 2025-03-09 12:33:26
*/
@Service
public class InvitationServiceImpl extends ServiceImpl<InvitationMapper, Invitation>
    implements InvitationService {

    @Resource
    private UserService userService;

    @Resource
    private JobService jobService;

    @Resource
    private HiringDataService hiringDataService;

    @Resource
    private ResumeRecordService resumeRecordService;

    @Override
    @Transactional
    public Boolean addInvitation(InvitationAddRequest invitationAddRequest, HttpServletRequest request) {
        Long recId = userService.getLoginUser(request).getId();
        Long canId = invitationAddRequest.getCanId();
        Long jobId = invitationAddRequest.getJobId();
        if (recId == null || canId == null || jobId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数不能为空");
        }
        User candidate = userService.getById(canId);
        if (candidate == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "求职者数据不存在");
        }
        Job job = jobService.getById(jobId);
        if (job == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "职位数据不存在");
        }
        String message = invitationAddRequest.getMessage();
        if (StringUtils.isBlank(message)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "面试邀请信息不能为空");
        }
        Invitation exist = this.getOne(new QueryWrapper<Invitation>().eq("job_id", jobId).eq("can_id", canId));
        if (exist != null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "已向该求职者发送过面试邀请");
        }
        Invitation invitation = new Invitation();
        invitation.setRecId(recId);
        invitation.setCanId(canId);
        invitation.setJobId(jobId);
        invitation.setMessage(message);
        // 设置面试邀请状态为已发送
        invitation.setStatus(InvitationStatus.SENT);

        HiringData hiringData = hiringDataService.getOne(new QueryWrapper<HiringData>().eq("job_id", jobId));
        if (hiringData == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "招聘数据不存在");
        }
        // 简历初筛通过数量加1
        hiringData.setApplicationPassCount(hiringData.getApplicationPassCount() + 1);
        boolean update = hiringDataService.updateById(hiringData);
        if (!update) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新招聘数据失败");
        }

        boolean result = this.save(invitation);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "发送面试邀请失败");
        }
        return true;
    }

    @Override
    @Transactional
    public Boolean acceptInvitation(InvitationIdRequest invitationIdRequest, HttpServletRequest request) {
        Long id = invitationIdRequest.getId();
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数不能为空");
        }
        Invitation invitation = this.getById(id);
        if (invitation == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "面试邀请数据不存在");
        }
        if (invitation.getStatus().equals(InvitationStatus.REJECTED)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "您已拒绝该面试邀请");
        }
        if (invitation.getStatus().equals(InvitationStatus.ACCEPTED)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "您已接受该面试邀请");
        }
        // 设置面试邀请状态为已接受
        invitation.setStatus(InvitationStatus.ACCEPTED);
        boolean result = this.updateById(invitation);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "接受面试邀请失败");
        }

        Long jobId = invitation.getJobId();
        HiringData hiringData = hiringDataService.getOne(new QueryWrapper<HiringData>().eq("job_id", jobId));
        if (hiringData == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "招聘数据不存在");
        }
        // 面试候选人数量+1
        hiringData.setInterviewCount(hiringData.getInterviewCount() + 1);
        boolean update = hiringDataService.updateById(hiringData);
        if (!update) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新招聘数据失败");
        }

        Long userId = userService.getLoginUser(request).getId();
        ResumeRecord resumeRecord = resumeRecordService.getOne(new QueryWrapper<ResumeRecord>().eq("user_id", userId).eq("job_id", jobId));
        if (resumeRecord == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "投递记录数据不存在");
        }
        // 设置投递记录状态为面试流程中
        resumeRecord.setStatus(HiringStatusConstant.INTERVIEWING);
        boolean save = resumeRecordService.updateById(resumeRecord);
        if (!save) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新投递记录失败");
        }

        return true;
    }

    @Override
    public Boolean rejectInvitation(InvitationIdRequest invitationIdRequest, HttpServletRequest request) {
        Long id = invitationIdRequest.getId();
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数不能为空");
        }
        Invitation invitation = this.getById(id);
        if (invitation == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "面试邀请数据不存在");
        }
        if (invitation.getStatus().equals(InvitationStatus.ACCEPTED)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "您已接受该面试邀请");
        }
        if (invitation.getStatus().equals(InvitationStatus.REJECTED)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "您已拒绝该面试邀请");
        }
        // 设置面试邀请状态为已拒绝
        invitation.setStatus(InvitationStatus.REJECTED);
        boolean update = this.updateById(invitation);
        if (!update) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "拒绝面试邀请失败");
        }

        Long jobId = invitation.getJobId();
        Long userId = userService.getLoginUser(request).getId();
        ResumeRecord resumeRecord = resumeRecordService.getOne(new QueryWrapper<ResumeRecord>().eq("user_id", userId).eq("job_id", jobId));
        if (resumeRecord == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "投递记录数据不存在");
        }
        // 设置投递记录状态为流程结束
        resumeRecord.setStatus(HiringStatusConstant.FINISHED);
        boolean save = resumeRecordService.updateById(resumeRecord);
        if (!save) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新投递记录失败");
        }

        return true;
    }

    @Override
    public QueryWrapper<Invitation> getInvitationQueryWrapper(InvitationQueryRequest invitationQueryRequest) {
        if (invitationQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long recId = invitationQueryRequest.getRecId();
        Long canId = invitationQueryRequest.getCanId();
        Long jobId = invitationQueryRequest.getJobId();
        Integer status = invitationQueryRequest.getStatus();
        String sortField = invitationQueryRequest.getSortField();
        String sortOrder = invitationQueryRequest.getSortOrder();
        QueryWrapper<Invitation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(recId != null, "rec_id", recId);
        queryWrapper.eq(canId != null, "can_id", canId);
        queryWrapper.eq(jobId != null, "job_id", jobId);
        queryWrapper.eq(status != null, "status", status);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }


}




