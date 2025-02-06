package com.shaoyafan.jobhubbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shaoyafan.jobhubbackend.model.domain.Status;
import com.shaoyafan.jobhubbackend.service.StatusService;
import com.shaoyafan.jobhubbackend.mapper.StatusMapper;
import org.springframework.stereotype.Service;

/**
* @author SYF
* @description 针对表【status(状态表)】的数据库操作Service实现
* @createDate 2025-01-26 16:39:45
*/
@Service
public class StatusServiceImpl extends ServiceImpl<StatusMapper, Status>
    implements StatusService {

}




