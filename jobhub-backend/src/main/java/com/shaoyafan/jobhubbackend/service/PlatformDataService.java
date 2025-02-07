package com.shaoyafan.jobhubbackend.service;

import com.shaoyafan.jobhubbackend.model.domain.PlatformData;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author SYF
* @description 针对表【platform_data(平台数据表)】的数据库操作Service
* @createDate 2025-01-26 16:39:45
*/
public interface PlatformDataService extends IService<PlatformData> {

    /**
     * 获取平台数据
     *
     * @return
     */
    PlatformData getPlatformData();

}
