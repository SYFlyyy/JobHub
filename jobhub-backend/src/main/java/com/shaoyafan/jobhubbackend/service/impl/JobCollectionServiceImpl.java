package com.shaoyafan.jobhubbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shaoyafan.jobhubbackend.common.ErrorCode;
import com.shaoyafan.jobhubbackend.constant.StatusConstant;
import com.shaoyafan.jobhubbackend.exception.BusinessException;
import com.shaoyafan.jobhubbackend.model.domain.HiringData;
import com.shaoyafan.jobhubbackend.model.domain.Job;
import com.shaoyafan.jobhubbackend.model.domain.JobCollection;
import com.shaoyafan.jobhubbackend.model.dto.job.JobIdRequest;
import com.shaoyafan.jobhubbackend.service.HiringDataService;
import com.shaoyafan.jobhubbackend.service.JobCollectionService;
import com.shaoyafan.jobhubbackend.mapper.JobCollectionMapper;
import com.shaoyafan.jobhubbackend.service.JobService;
import com.shaoyafan.jobhubbackend.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
* @author SYF
* @description 针对表【job_collection(职位收藏表)】的数据库操作Service实现
* @createDate 2025-01-26 16:39:45
*/
@Service
public class JobCollectionServiceImpl extends ServiceImpl<JobCollectionMapper, JobCollection>
    implements JobCollectionService {

    @Resource
    private UserService userService;

    @Resource
    private JobService jobService;

    @Resource
    private HiringDataService hiringDataService;

    @Override
    @Transactional
    public Boolean addJobCollection(JobIdRequest jobIdRequest, HttpServletRequest request) {
        Long userId = userService.getLoginUser(request).getId();
        Long jobId = jobIdRequest.getId();
        Job job = jobService.getById(jobId);
        if (job == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "职位不存在");
        }
        QueryWrapper<JobCollection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("job_id", jobId);
        JobCollection jobCollection = this.getOne(queryWrapper);
        if (jobCollection == null) {
            jobCollection = new JobCollection();
            jobCollection.setUserId(userId);
            jobCollection.setJobId(jobId);
            // 默认状态为正常
            jobCollection.setStatus(StatusConstant.NORMAL);
            boolean result = this.save(jobCollection);
            if (!result) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR);
            }
        } else {
            if (Objects.equals(jobCollection.getStatus(), StatusConstant.NORMAL)) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "职位已经收藏");
            } else {
                jobCollection.setStatus(StatusConstant.NORMAL);
                boolean result = this.updateById(jobCollection);
                if (!result) {
                    throw new BusinessException(ErrorCode.OPERATION_ERROR);
                }
            }
        }
        // 招聘数据收藏数+1
        HiringData hiringData = hiringDataService.getOne(new QueryWrapper<HiringData>().eq("job_id", jobId));
        if (hiringData == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        hiringData.setFavoriteCount(hiringData.getFavoriteCount() + 1);
        boolean save = hiringDataService.updateById(hiringData);
        if (!save) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return true;
    }

    @Override
    @Transactional
    public Boolean cancelJobCollection(JobIdRequest jobIdRequest, HttpServletRequest request) {
        Long userId = userService.getLoginUser(request).getId();
        Long jobId = jobIdRequest.getId();
        QueryWrapper<JobCollection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("job_id", jobId);
        JobCollection jobCollection = this.getOne(queryWrapper);
        if (jobCollection == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "职位未收藏");
        }
        if (Objects.equals(jobCollection.getStatus(), StatusConstant.DISABLED)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "职位已取消收藏");
        }
        // 设置状态为禁用
        jobCollection.setStatus(StatusConstant.DISABLED);
        boolean result = this.updateById(jobCollection);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        // 招聘数据收藏数-1
        HiringData hiringData = hiringDataService.getOne(new QueryWrapper<HiringData>().eq("job_id", jobId));
        if (hiringData == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        hiringData.setFavoriteCount(hiringData.getFavoriteCount() - 1);
        boolean save = hiringDataService.updateById(hiringData);
        if (!save) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return true;
    }
}