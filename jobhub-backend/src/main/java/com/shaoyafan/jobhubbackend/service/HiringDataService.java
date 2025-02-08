package com.shaoyafan.jobhubbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shaoyafan.jobhubbackend.model.domain.HiringData;
import com.shaoyafan.jobhubbackend.model.dto.job.JobIdRequest;
import com.shaoyafan.jobhubbackend.model.vo.hiringData.HiringDataVO;

/**
* @author SYF
* @description 针对表【hiring_data(招聘数据表)】的数据库操作Service
* @createDate 2025-01-26 16:39:45
*/
public interface HiringDataService extends IService<HiringData> {

    /**
     * 获取招聘数据
     *
     * @param jobIdRequest
     * @return
     */
    HiringDataVO getHiringData(JobIdRequest jobIdRequest);

}
