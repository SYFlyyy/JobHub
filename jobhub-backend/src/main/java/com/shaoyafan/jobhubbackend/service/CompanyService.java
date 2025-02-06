package com.shaoyafan.jobhubbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shaoyafan.jobhubbackend.model.domain.Company;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shaoyafan.jobhubbackend.model.dto.company.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 企业服务
 *
* @author SYF
* @description 针对表【company(企业信息表)】的数据库操作Service
* @createDate 2025-01-26 16:39:45
*/
public interface CompanyService extends IService<Company> {

    /**
     * 企业注册
     *
     * @param companyAddRequest
     * @param request
     * @return
     */
    Long addCompany(CompanyAddRequest companyAddRequest, HttpServletRequest request);

    /**
     * 更新企业
     *
     * @param companyUpdateRequest
     * @return
     */
    Boolean updateCompany(CompanyUpdateRequest companyUpdateRequest);

    /**
     * 用户绑定企业
     *
     * @param companyBindRequest
     * @param request
     * @return
     */
    Boolean bindCompany(CompanyBindRequest companyBindRequest, HttpServletRequest request);

    /**
     * 企业审核
     *
     * @param id
     * @return
     */
    Boolean reviewCompany(Long id);

    /**
     * 企业冻结
     *
     * @param id
     * @return
     */
    Boolean freezeCompany(Long id);

    /**
     * 获取查询条件
     *
     * @param companyQueryRequest
     * @return
     */
    QueryWrapper<Company> getCompanyQueryWrapper(CompanyQueryRequest companyQueryRequest);
}
