package com.shaoyafan.jobhubbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shaoyafan.jobhubbackend.common.ErrorCode;
import com.shaoyafan.jobhubbackend.exception.BusinessException;
import com.shaoyafan.jobhubbackend.model.domain.PlatformData;
import com.shaoyafan.jobhubbackend.model.domain.User;
import com.shaoyafan.jobhubbackend.model.enums.UserRoleEnum;
import com.shaoyafan.jobhubbackend.service.CompanyService;
import com.shaoyafan.jobhubbackend.service.JobService;
import com.shaoyafan.jobhubbackend.service.PlatformDataService;
import com.shaoyafan.jobhubbackend.mapper.PlatformDataMapper;
import com.shaoyafan.jobhubbackend.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author SYF
* @description 针对表【platform_data(平台数据表)】的数据库操作Service实现
* @createDate 2025-01-26 16:39:45
*/
@Service
public class PlatformDataServiceImpl extends ServiceImpl<PlatformDataMapper, PlatformData>
    implements PlatformDataService {

    @Resource
    private UserService userService;

    @Resource
    private CompanyService companyService;

    @Resource
    private JobService jobService;

    @Override
    public PlatformData getPlatformData() {
        long candidateCount = userService.count(new QueryWrapper<User>().eq("role", UserRoleEnum.CANDIDATE.getValue()));
        long recruiterCount = userService.count(new QueryWrapper<User>().eq("role", UserRoleEnum.RECRUITER.getValue()));
        long companyCount = companyService.count();
        long jobCount = jobService.count();
        PlatformData platformData = new PlatformData();
        platformData.setCandidateCount(candidateCount);
        platformData.setRecruiterCount(recruiterCount);
        platformData.setCompanyCount(companyCount);
        platformData.setJobCount(jobCount);
        boolean result = this.save(platformData);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "获取平台数据失败");
        }
        return platformData;
    }
}




