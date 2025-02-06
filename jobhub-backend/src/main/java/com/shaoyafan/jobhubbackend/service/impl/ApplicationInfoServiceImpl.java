package com.shaoyafan.jobhubbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shaoyafan.jobhubbackend.model.domain.ApplicationInfo;
import com.shaoyafan.jobhubbackend.service.ApplicationInfoService;
import com.shaoyafan.jobhubbackend.mapper.ApplicationInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author SYF
* @description 针对表【application_info(在线简历表)】的数据库操作Service实现
* @createDate 2025-01-26 16:39:45
*/
@Service
public class ApplicationInfoServiceImpl extends ServiceImpl<ApplicationInfoMapper, ApplicationInfo>
    implements ApplicationInfoService {

}




