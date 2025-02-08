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
        HiringData hiringData = this.getOne(new QueryWrapper<HiringData>().eq("job_id", jobIdRequest.getId()));
        if (hiringData == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "招聘数据不存在");
        }
        HiringDataVO hiringDataVO = new HiringDataVO();
        BeanUtils.copyProperties(hiringData, hiringDataVO);
        // 面试数为0时，直接返回0.00%
        if (hiringData.getInterviewCount() == 0) {
            hiringDataVO.setInterviewRate("0.00%");
            return hiringDataVO;
        }
        // 计算面试转换率
        Double rate = Double.valueOf(hiringData.getHiredCount()) / hiringData.getInterviewCount() * 100;
        String interviewRate = String.format("%.2f", rate) + "%";
        hiringDataVO.setInterviewRate(interviewRate);
        return hiringDataVO;
    }
}




