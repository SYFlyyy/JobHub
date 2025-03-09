package com.shaoyafan.jobhubbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shaoyafan.jobhubbackend.common.ErrorCode;
import com.shaoyafan.jobhubbackend.exception.BusinessException;
import com.shaoyafan.jobhubbackend.mapper.HiringDataMapper;
import com.shaoyafan.jobhubbackend.model.domain.HiringData;
import com.shaoyafan.jobhubbackend.model.dto.job.JobIdRequest;
import com.shaoyafan.jobhubbackend.model.vo.hiringData.HiringDataVO;
import com.shaoyafan.jobhubbackend.service.HiringDataService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
* @author SYF
* @description 针对表【hiring_data(招聘数据表)】的数据库操作Service实现
* @createDate 2025-01-26 16:39:45
*/
@Service
public class HiringDataServiceImpl extends ServiceImpl<HiringDataMapper, HiringData>
    implements HiringDataService {

    @Override
    public HiringDataVO getHiringData(JobIdRequest jobIdRequest) {
        // 1. 查询基础数据
        HiringData hiringData = this.getOne(new QueryWrapper<HiringData>()
                .eq("job_id", jobIdRequest.getId()));
        if (hiringData == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "招聘数据不存在");
        }

        // 2. 复制基础属性
        HiringDataVO hiringDataVO = new HiringDataVO();
        BeanUtils.copyProperties(hiringData, hiringDataVO);

        // 3. 安全获取数值（防止空指针）
        long applicationCount = Optional.ofNullable(hiringData.getApplicationCount()).orElse(0L);
        long applicationPassCount = Optional.ofNullable(hiringData.getApplicationPassCount()).orElse(0L);
        long interviewCount = Optional.ofNullable(hiringData.getInterviewCount()).orElse(0L);
        long interviewPassCount = Optional.ofNullable(hiringData.getInterviewPassCount()).orElse(0L);
        long intentionCount = Optional.ofNullable(hiringData.getIntentionCount()).orElse(0L);
        long hiredCount = Optional.ofNullable(hiringData.getHiredCount()).orElse(0L);

        // 4. 计算通过率（带安全校验）
        // 简历初筛通过率 = 初筛通过量 / 投递量
        hiringDataVO.setApplicationPassRate(
                calculateRate(applicationPassCount, applicationCount)
        );

        // 面试通过率 = 面试通过量 / 面试候选人量
        hiringDataVO.setInterviewPassRate(
                calculateRate(interviewPassCount, interviewCount)
        );

        // 最终录用率 = 录用数量 / 意向数量
        hiringDataVO.setHiredPassRate(
                calculateRate(hiredCount, intentionCount)
        );

        // 全流程转化率 = 录用数量 / 投递量
        hiringDataVO.setTotalConversionRate(
                calculateRate(hiredCount, applicationCount) // 复用之前的计算方法
        );

        return hiringDataVO;
    }

    private String calculateRate(long numerator, long denominator) {
        if (denominator == 0L) {
            return "0.00%"; // 或根据需求返回 "N/A"
        }
        double rate = (numerator * 100.0) / denominator;
        return String.format("%.2f%%", rate);
    }
}




