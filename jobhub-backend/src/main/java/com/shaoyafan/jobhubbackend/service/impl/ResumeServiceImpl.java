package com.shaoyafan.jobhubbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shaoyafan.jobhubbackend.model.domain.Resume;
import com.shaoyafan.jobhubbackend.service.ResumeService;
import com.shaoyafan.jobhubbackend.mapper.ResumeMapper;
import org.springframework.stereotype.Service;

/**
* @author SYF
* @description 针对表【resume(简历附件表)】的数据库操作Service实现
* @createDate 2025-01-26 16:39:45
*/
@Service
public class ResumeServiceImpl extends ServiceImpl<ResumeMapper, Resume>
    implements ResumeService {

}




