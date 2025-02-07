package com.shaoyafan.jobhubbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shaoyafan.jobhubbackend.common.ErrorCode;
import com.shaoyafan.jobhubbackend.constant.CommonConstant;
import com.shaoyafan.jobhubbackend.constant.HiringStatusConstant;
import com.shaoyafan.jobhubbackend.constant.StatusConstant;
import com.shaoyafan.jobhubbackend.exception.BusinessException;
import com.shaoyafan.jobhubbackend.model.domain.Job;
import com.shaoyafan.jobhubbackend.model.domain.Resume;
import com.shaoyafan.jobhubbackend.model.domain.ResumeRecord;
import com.shaoyafan.jobhubbackend.model.dto.job.JobIdRequest;
import com.shaoyafan.jobhubbackend.model.dto.resumeRecord.ResumeRecordQueryRequest;
import com.shaoyafan.jobhubbackend.service.JobService;
import com.shaoyafan.jobhubbackend.service.ResumeRecordService;
import com.shaoyafan.jobhubbackend.mapper.ResumeRecordMapper;
import com.shaoyafan.jobhubbackend.service.ResumeService;
import com.shaoyafan.jobhubbackend.service.UserService;
import com.shaoyafan.jobhubbackend.utils.SqlUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
* @author SYF
* @description 针对表【resume_record(投递记录表)】的数据库操作Service实现
* @createDate 2025-01-26 16:39:45
*/
@Service
public class ResumeRecordServiceImpl extends ServiceImpl<ResumeRecordMapper, ResumeRecord>
    implements ResumeRecordService {

    @Resource
    private UserService userService;

    @Resource
    private JobService jobService;

    @Resource
    private ResumeService resumeService;

    @Override
    public Boolean addResumeRecord(JobIdRequest jobIdRequest, HttpServletRequest request) {
        Long jobId = jobIdRequest.getId();
        Job job = jobService.getById(jobId);
        if (job == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if (Objects.equals(job.getStatus(), StatusConstant.DISABLED)) {
            throw new BusinessException(ErrorCode.STATE_ERROR, "该职位已下线");
        }
        Long userId = userService.getLoginUser(request).getId();
        // 判断是否已经投递过该职位
        ResumeRecord existResumeRecord = this.getOne(new QueryWrapper<ResumeRecord>().eq("job_id", jobId).eq("user_id", userId));
        if (existResumeRecord != null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "已投递过该职位");
        }
        ResumeRecord resumeRecord = new ResumeRecord();
        resumeRecord.setJobId(jobId);
        resumeRecord.setUserId(userId);
        Resume resume = resumeService.getOne(new QueryWrapper<Resume>().eq("user_id", userId));
        if (resume != null) {
            resumeRecord.setResumeId(resume.getId());
        }
        // 默认状态为已投递
        resumeRecord.setStatus(HiringStatusConstant.APPLIED);
        boolean result = this.save(resumeRecord);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return true;
    }

    @Override
    public QueryWrapper<ResumeRecord> getQueryWrapper(ResumeRecordQueryRequest resumeRecordQueryRequest) {
        if (resumeRecordQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = resumeRecordQueryRequest.getId();
        Long jobId = resumeRecordQueryRequest.getJobId();
        Long userId = resumeRecordQueryRequest.getUserId();
        Long resumeId = resumeRecordQueryRequest.getResumeId();
        Integer status = resumeRecordQueryRequest.getStatus();
        QueryWrapper<ResumeRecord> queryWrapper = new QueryWrapper<>();
        String sortField = resumeRecordQueryRequest.getSortField();
        String sortOrder = resumeRecordQueryRequest.getSortOrder();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(jobId != null, "job_id", jobId);
        queryWrapper.eq(userId != null, "user_id", userId);
        queryWrapper.eq(resumeId != null, "resume_id", resumeId);
        queryWrapper.eq(status != null, "status", status);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }
}