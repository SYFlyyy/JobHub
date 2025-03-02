package com.shaoyafan.jobhubbackend.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shaoyafan.jobhubbackend.common.ErrorCode;
import com.shaoyafan.jobhubbackend.constant.CommonConstant;
import com.shaoyafan.jobhubbackend.constant.StatusConstant;
import com.shaoyafan.jobhubbackend.exception.BusinessException;
import com.shaoyafan.jobhubbackend.mapper.JobCollectionMapper;
import com.shaoyafan.jobhubbackend.mapper.JobMapper;
import com.shaoyafan.jobhubbackend.model.domain.*;
import com.shaoyafan.jobhubbackend.model.dto.job.JobAddRequest;
import com.shaoyafan.jobhubbackend.model.dto.job.JobIdRequest;
import com.shaoyafan.jobhubbackend.model.dto.job.JobQueryRequest;
import com.shaoyafan.jobhubbackend.model.dto.job.JobUpdateRequest;
import com.shaoyafan.jobhubbackend.model.vo.job.JobVO;
import com.shaoyafan.jobhubbackend.service.*;
import com.shaoyafan.jobhubbackend.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
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
    private JobMapper jobMapper;

    @Resource
    private JobCollectionMapper jobCollectionMapper;

    @Resource
    private UserService userService;

    @Resource
    private CompanyService companyService;

    @Resource
    private HiringDataService hiringDataService;

    @Override
    @Transactional
    public Long addJob(JobAddRequest jobAddRequest, HttpServletRequest request) {
        Long companyId = userService.getLoginUser(request).getCompanyId();
        if (companyId == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "当前用户未绑定企业");
        }
        Company company = companyService.getById(companyId);
        if (company == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "企业不存在");
        }
        if (Objects.equals(company.getStatus(), StatusConstant.PENDING)) {
            throw new BusinessException(ErrorCode.STATE_ERROR, "企业待审核中");
        }
        // String companyName = company.getName();
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
        if (StringUtils.isBlank(salary)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "薪资不能为空");
        }
        Job job = new Job();
        job.setCompanyId(companyId);
        // job.setCompanyName(companyName);
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
        // 新增招聘数据
        HiringData hiringData = new HiringData();
        hiringData.setJobId(job.getId());
        boolean save = hiringDataService.save(hiringData);
        if (!save) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "新增招聘数据失败");
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
        // 更新后状态变为待审核
        job.setStatus(StatusConstant.PENDING);
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
        if (Objects.equals(job.getStatus(), StatusConstant.REVIEWED)) {
            throw new BusinessException(ErrorCode.STATE_ERROR, "职位已通过审核");
        }
        // 审核通过
        job.setStatus(StatusConstant.REVIEWED);
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
        if (Objects.equals(job.getStatus(), StatusConstant.PENDING)) {
            throw new BusinessException(ErrorCode.STATE_ERROR, "职位待审核中");
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
    public Page<JobVO> listJobWithCompany(JobQueryRequest jobQueryRequest) {
        QueryWrapper<Job> jobQueryWrapper = this.getJobVOQueryWrapper(jobQueryRequest);
        long current = jobQueryRequest.getCurrent();
        long size = jobQueryRequest.getPageSize();
        Page<JobVO> page = new Page<>(current, size);
        IPage<JobVO> jobVOIPage = jobMapper.selectJobWithCompany(page, jobQueryWrapper);
        return (Page<JobVO>) jobVOIPage;
    }

    @Override
    public JobVO getJobWithCompanyById(JobIdRequest jobIdRequest) {
        if (jobIdRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = jobIdRequest.getId();
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "职位id不能为空");
        }
        JobVO jobVO = jobMapper.selectJobWithCompanyById(id);
        if (jobVO == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "职位不存在");
        }
        return jobVO;
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

    @Override
    public QueryWrapper<Job> getJobVOQueryWrapper(JobQueryRequest jobQueryRequest) {
        if (jobQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = jobQueryRequest.getId();
        Long companyId = jobQueryRequest.getCompanyId();
        String address = jobQueryRequest.getAddress();
        Integer type = jobQueryRequest.getType();
        String salary = jobQueryRequest.getSalary();
        String intro = jobQueryRequest.getIntro();
        Integer status = jobQueryRequest.getStatus();
        String searchKey = jobQueryRequest.getSearchKey();
        String sortField = jobQueryRequest.getSortField();
        String sortOrder = jobQueryRequest.getSortOrder();
        QueryWrapper<Job> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "job.id", id);
        queryWrapper.eq(companyId != null, "job.company_id", companyId);
        // 处理 searchKey 进行模糊查询
        if (StringUtils.isNotBlank(searchKey)) {
            String likeValue = "%" + searchKey + "%";
            queryWrapper.and(wrapper -> wrapper.like("job.name", likeValue)
                    .or()
                    .like("company.name", likeValue));
        }
        queryWrapper.eq(type != null, "job.type", type);
        queryWrapper.like(StringUtils.isNotBlank(address), "company.address", address);
        queryWrapper.like(StringUtils.isNotBlank(salary), "job.salary", salary);
        queryWrapper.like(StringUtils.isNotBlank(intro), "job.intro", intro);
        queryWrapper.eq(status != null, "job.status", status);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }

    @Override
    public Page<JobVO> getCollectedJobByPage(JobQueryRequest jobQueryRequest, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        Long userId = loginUser.getId();
        String sortField = jobQueryRequest.getSortField();
        String sortOrder = jobQueryRequest.getSortOrder();
        // 获取用户收藏的职位id
        List<Long> jobIds = jobCollectionMapper.getJobIdByUserId(userId);
        if (CollectionUtil.isEmpty(jobIds)) {
            return new Page<>();
        }
        QueryWrapper<Job> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("job.id", jobIds);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        long current = jobQueryRequest.getCurrent();
        long size = jobQueryRequest.getPageSize();
        Page<JobVO> page = new Page<>(current, size);
        IPage<JobVO> jobVOIPage = jobMapper.selectJobWithCompany(page, queryWrapper);
        return (Page<JobVO>) jobVOIPage;
    }
}




