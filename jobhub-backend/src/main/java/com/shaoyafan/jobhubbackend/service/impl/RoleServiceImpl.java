package com.shaoyafan.jobhubbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shaoyafan.jobhubbackend.model.domain.Role;
import com.shaoyafan.jobhubbackend.service.RoleService;
import com.shaoyafan.jobhubbackend.mapper.RoleMapper;
import org.springframework.stereotype.Service;

/**
* @author SYF
* @description 针对表【role(角色表)】的数据库操作Service实现
* @createDate 2025-01-26 16:39:45
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService {

}




