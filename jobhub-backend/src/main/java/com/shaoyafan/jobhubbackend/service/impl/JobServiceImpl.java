package com.shaoyafan.jobhubbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shaoyafan.jobhubbackend.common.ErrorCode;
import com.shaoyafan.jobhubbackend.constant.CommonConstant;
import com.shaoyafan.jobhubbackend.constant.StatusConstant;
import com.shaoyafan.jobhubbackend.exception.BusinessException;
import com.shaoyafan.jobhubbackend.model.domain.Job;
import com.shaoyafan.jobhubbackend.model.domain.User;
import com.shaoyafan.jobhubbackend.model.dto.job.JobAddRequest;
import com.shaoyafan.jobhubbackend.model.dto.job.JobIdRequest;
import com.shaoyafan.jobhubbackend.model.dto.job.JobQueryRequest;
import com.shaoyafan.jobhubbackend.model.dto.job.JobUpdateRequest;
import com.shaoyafan.jobhubbackend.model.enums.UserRoleEnum;
import com.shaoyafan.jobhubbackend.service.JobService;
import com.shaoyafan.jobhubbackend.mapper.JobMapper;
import com.shaoyafan.jobhubbackend.service.UserService;
import com.shaoyafan.jobhubbackend.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
* @author SYF
* @description 针对表【job(职位表)】的数据库操作Service实现
* @createDate 2025-01-26 16:39:45
*/
@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job>
    implements JobService {

    @Resource
    private UserService userService;

    @Override
    public Long addJob(JobAddRequest jobAddRequest, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        Long companyId = loginUser.getCompanyId();
        if (companyId == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "当前用户未绑定企业");
        }
        String name = jobAddRequest.getName();
        if (StringUtils.isBlank(name)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "职位名称不能为空");
        }
        Integer type = jobAddRequest.getType();
        if (type == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "职位类型不能为空");
        }
        String intro = jobAddRequest.getIntro();
        if (StringUtils.isBlank(intro)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "职位详情不能为空");
        }
        String salary = jobAddRequest.getSalary();
        Job job = new Job();
        job.setCompanyId(companyId);
        job.setName(name);
        job.setType(type);
        job.setSalary(salary);
        job.setIntro(intro);
        // 默认为待审核状态
        job.setStatus(StatusConstant.PENDING);
        boolean result = this.save(job);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "新增职位失败");
        }
        return job.getId();
    }

    @Override
    public Boolean updateJob(JobUpdateRequest jobUpdateRequest) {
        Long id = jobUpdateRequest.getId();
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Job job = this.getById(id);
        if (job == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        String name = jobUpdateRequest.getName();
        if (StringUtils.isBlank(name)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "职位名称不能为空");
        }
        Integer type = jobUpdateRequest.getType();
        if (type == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "职位类型不能为空");
        }
        String intro = jobUpdateRequest.getIntro();
        if (StringUtils.isBlank(intro)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "职位详情不能为空");
        }
        String salary = jobUpdateRequest.getSalary();
        job.setName(name);
        job.setType(type);
        job.setIntro(intro);
        job.setSalary(salary);
        boolean result = this.updateById(job);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return true;
    }

    @Override
    public Boolean reviewJob(JobIdRequest jobIdRequest) {
        Long id = jobIdRequest.getId();
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Job job = this.getById(id);
        if (job == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if (Objects.equals(job.getStatus(), StatusConstant.NORMAL)) {
            throw new BusinessException(ErrorCode.STATE_ERROR, "职位已通过审核");
        }
        // 审核通过
        job.setStatus(StatusConstant.NORMAL);
        boolean result = this.updateById(job);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return true;
    }

    @Override
    public Boolean onlineJob(JobIdRequest jobIdRequest) {
        Long id = jobIdRequest.getId();
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Job job = this.getById(id);
        if (job == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if (Objects.equals(job.getStatus(), StatusConstant.PENDING)) {
            throw new BusinessException(ErrorCode.STATE_ERROR, "职位待审核中");
        }
        if (Objects.equals(job.getStatus(), StatusConstant.NORMAL)) {
            throw new BusinessException(ErrorCode.STATE_ERROR, "职位已上线");
        }
        // 上线
        job.setStatus(StatusConstant.NORMAL);
        boolean result = this.updateById(job);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return true;
    }

    @Override
    public Boolean offlineJob(JobIdRequest jobIdRequest) {
        Long id = jobIdRequest.getId();
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Job job = this.getById(id);
        if (job == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if (Objects.equals(job.getStatus(), StatusConstant.DISABLED)) {
            throw new BusinessException(ErrorCode.STATE_ERROR, "职位已下线");
        }
        // 下线
        job.setStatus(StatusConstant.DISABLED);
        boolean result = this.updateById(job);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return true;
    }

    @Override
    public QueryWrapper<Job> getJobQueryWrapper(JobQueryRequest jobQueryRequest) {
        if (jobQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = jobQueryRequest.getId();
        Long companyId = jobQueryRequest.getCompanyId();
        String name = jobQueryRequest.getName();
        Integer type = jobQueryRequest.getType();
        String salary = jobQueryRequest.getSalary();
        String intro = jobQueryRequest.getIntro();
        Integer status = jobQueryRequest.getStatus();
        String sortField = jobQueryRequest.getSortField();
        String sortOrder = jobQueryRequest.getSortOrder();
        QueryWrapper<Job> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(companyId != null, "company_id", companyId);
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.eq(type != null, "type", type);
        queryWrapper.like(StringUtils.isNotBlank(salary), "salary", salary);
        queryWrapper.like(StringUtils.isNotBlank(intro), "intro", intro);
        queryWrapper.eq(status != null, "status", status);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }

}




