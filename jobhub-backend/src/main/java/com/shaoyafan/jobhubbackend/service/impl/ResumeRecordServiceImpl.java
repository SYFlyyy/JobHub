package com.shaoyafan.jobhubbackend.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shaoyafan.jobhubbackend.common.ErrorCode;
import com.shaoyafan.jobhubbackend.constant.CommonConstant;
import com.shaoyafan.jobhubbackend.constant.HiringStatusConstant;
import com.shaoyafan.jobhubbackend.constant.StatusConstant;
import com.shaoyafan.jobhubbackend.exception.BusinessException;
import com.shaoyafan.jobhubbackend.model.domain.*;
import com.shaoyafan.jobhubbackend.model.dto.job.JobIdRequest;
import com.shaoyafan.jobhubbackend.model.dto.resumeRecord.ResumeRecordQueryRequest;
import com.shaoyafan.jobhubbackend.model.dto.resumeRecord.ResumeRecordUpdateStatusRequest;
import com.shaoyafan.jobhubbackend.model.vo.resumeRecord.ResumeRecordVO;
import com.shaoyafan.jobhubbackend.model.vo.resumeRecord.UserResumeRecordVO;
import com.shaoyafan.jobhubbackend.service.*;
import com.shaoyafan.jobhubbackend.mapper.ResumeRecordMapper;
import com.shaoyafan.jobhubbackend.utils.SqlUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Resource
    private ApplicationInfoService applicationInfoService;

    @Resource
    private HiringDataService hiringDataService;

    @Resource
    private CompanyService companyService;

    @Override
    @Transactional
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
        ApplicationInfo applicationInfo = applicationInfoService.getOne(new QueryWrapper<ApplicationInfo>().eq("user_id", userId));
        if (applicationInfo == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "请先完善个人在线简历");
        }
        Resume resume = resumeService.getOne(new QueryWrapper<Resume>().eq("user_id", userId));
        if (resume == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "请先上传个人简历附件");
        }
        // 判断是否已经投递过该职位
        ResumeRecord existResumeRecord = this.getOne(new QueryWrapper<ResumeRecord>().eq("job_id", jobId).eq("user_id", userId));
        if (existResumeRecord != null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "已投递过该职位");
        }
        ResumeRecord resumeRecord = new ResumeRecord();
        resumeRecord.setJobId(jobId);
        resumeRecord.setUserId(userId);
        resumeRecord.setResumeId(resume.getId());
        // 默认状态为已投递
        resumeRecord.setStatus(HiringStatusConstant.APPLIED);
        boolean result = this.save(resumeRecord);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        // 招聘数据投递数+1
        HiringData hiringData = hiringDataService.getOne(new QueryWrapper<HiringData>().eq("job_id", jobId));
        if (hiringData == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        hiringData.setApplicationCount(hiringData.getApplicationCount() + 1);
        boolean save = hiringDataService.updateById(hiringData);
        if (!save) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return true;
    }

    @Override
    public Boolean updateResumeRecordStatus(ResumeRecordUpdateStatusRequest resumeRecordUpdateStatusRequest) {
        // 招聘状态(0-已投递、1-面试中、2-面试通过、3-录用意向、4-已录用、5-流程结束)
        Long resumeRecordIdRequestId = resumeRecordUpdateStatusRequest.getId();
        ResumeRecord resumeRecord = this.getById(resumeRecordIdRequestId);
        if (resumeRecord == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        Integer currentStatus = resumeRecord.getStatus();
        // 需要更新的状态
        Integer status = resumeRecordUpdateStatusRequest.getStatus();
        if (status <= currentStatus) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "不能修改为当前状态或已结束状态");
        }
        if (Objects.equals(resumeRecord.getStatus(), HiringStatusConstant.FINISHED)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "当前流程已结束");
        }
        if (Objects.equals(status, HiringStatusConstant.APPLIED)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "不能修改为已投递状态");
        }
        resumeRecord.setStatus(status);
        boolean result = this.updateById(resumeRecord);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        // 更新招聘数据
        Long jobId = resumeRecord.getJobId();
        HiringData hiringData = hiringDataService.getOne(new QueryWrapper<HiringData>().eq("job_id", jobId));
        if (hiringData == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if (Objects.equals(status, HiringStatusConstant.INTERVIEWING)) {
            // 面试数+1
            hiringData.setInterviewCount(hiringData.getInterviewCount() + 1);
        } else if (Objects.equals(status, HiringStatusConstant.HIRED)) {
            // 录用数+1
            hiringData.setHiredCount(hiringData.getHiredCount() + 1);
        }
        boolean save = hiringDataService.updateById(hiringData);
        if (!save) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return true;
    }

    @Override
    public Page<ResumeRecordVO> getResumeRecordVOPage(Page<ResumeRecord> resumeRecordPage) {
        List<ResumeRecord> resumeRecordList = resumeRecordPage.getRecords();
        Page<ResumeRecordVO> resumeRecordVOPage = new Page<>(resumeRecordPage.getCurrent(), resumeRecordPage.getSize());
        resumeRecordVOPage.setTotal(resumeRecordPage.getTotal());
        if (CollectionUtil.isEmpty(resumeRecordList)) {
            return resumeRecordVOPage;
        }
        List<ResumeRecordVO> resumeRecordVOList = resumeRecordList.stream()
                .map(resumeRecord -> {
                    ResumeRecordVO resumeRecordVO = new ResumeRecordVO();
                    BeanUtils.copyProperties(resumeRecord, resumeRecordVO);
                    // 获取求职者姓名
                    ApplicationInfo applicationInfo = applicationInfoService.getOne(new QueryWrapper<ApplicationInfo>().
                            eq("user_id", resumeRecord.getUserId()));
                    if (applicationInfo != null) {
                        resumeRecordVO.setUserName(applicationInfo.getName());
                    }
                    Job job = jobService.getById(resumeRecord.getJobId());
                    if (job != null) {
                        resumeRecordVO.setJobName(job.getName());
                    }
                    return resumeRecordVO;
                }).collect(Collectors.toList());
        return resumeRecordVOPage.setRecords(resumeRecordVOList);
    }

    @Override
    public Page<UserResumeRecordVO> getUserResumeRecordVOPage(Page<ResumeRecord> resumeRecordPage) {
        List<ResumeRecord> resumeRecordList = resumeRecordPage.getRecords();
        Page<UserResumeRecordVO> userResumeRecordVOPage = new Page<>(resumeRecordPage.getCurrent(), resumeRecordPage.getSize());
        userResumeRecordVOPage.setTotal(resumeRecordPage.getTotal());
        if (CollectionUtil.isEmpty(resumeRecordList)) {
            return userResumeRecordVOPage;
        }
        List<UserResumeRecordVO> userResumeRecordVOList = resumeRecordList.stream()
                .map(resumeRecord -> {
                    UserResumeRecordVO userResumeRecordVO = new UserResumeRecordVO();
                    BeanUtils.copyProperties(resumeRecord, userResumeRecordVO);
                    Job job = jobService.getById(resumeRecord.getJobId());
                    if (job != null) {
                        Long companyId = job.getCompanyId();
                        Company company = companyService.getOne(new QueryWrapper<Company>().eq("id", companyId));
                        userResumeRecordVO.setCompanyName(company.getName());
                        userResumeRecordVO.setJobName(job.getName());
                        userResumeRecordVO.setSalary(job.getSalary());
                    }
                    return userResumeRecordVO;
                }).collect(Collectors.toList());
        return userResumeRecordVOPage.setRecords(userResumeRecordVOList);
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