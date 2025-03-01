package com.shaoyafan.jobhubbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shaoyafan.jobhubbackend.common.ErrorCode;
import com.shaoyafan.jobhubbackend.constant.CommonConstant;
import com.shaoyafan.jobhubbackend.constant.StatusConstant;
import com.shaoyafan.jobhubbackend.exception.BusinessException;
import com.shaoyafan.jobhubbackend.model.domain.Company;
import com.shaoyafan.jobhubbackend.model.domain.User;
import com.shaoyafan.jobhubbackend.model.dto.company.*;
import com.shaoyafan.jobhubbackend.service.CompanyService;
import com.shaoyafan.jobhubbackend.mapper.CompanyMapper;
import com.shaoyafan.jobhubbackend.service.UserService;
import com.shaoyafan.jobhubbackend.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
* @author SYF
* @description 针对表【company(企业信息表)】的数据库操作Service实现
* @createDate 2025-01-26 16:39:45
*/
@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company>
    implements CompanyService {

    @Resource
    private UserService userService;

    private static final String LOGO_FILE_PATH = "file/companyLogo/";

    @Override
    @Transactional
    public Long addCompany(CompanyAddRequest companyAddRequest, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        Long companyId = loginUser.getCompanyId();
        if (companyId != null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "当前用户已经绑定企业");
        }
        String name = companyAddRequest.getName();
        if (StringUtils.isBlank(name)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "企业名称不能为空");
        }
        Company sameNameCompany = this.getOne(new QueryWrapper<Company>().eq("name", name));
        if (sameNameCompany != null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "企业名称已经存在");
        }
        String intro = companyAddRequest.getIntro();
        if (StringUtils.isBlank(intro)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "企业简介不能为空");
        }
        String address = companyAddRequest.getAddress();
        if (StringUtils.isBlank(address)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "企业地址不能为空");
        }
        String type = companyAddRequest.getType();
        if (StringUtils.isBlank(type)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "企业类型不能为空");
        }
        String size = companyAddRequest.getSize();
        if (StringUtils.isBlank(size)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "企业规模不能为空");
        }
        String logo = companyAddRequest.getLogo();
        Company company = new Company();
        company.setName(name);
        company.setIntro(intro);
        company.setAddress(address);
        company.setType(type);
        company.setSize(size);
        company.setLogo(logo);
        // 默认状态为待审核
        company.setStatus(StatusConstant.PENDING);
        boolean result = this.save(company);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "企业注册失败");
        }
        companyId = company.getId();
        loginUser.setCompanyId(companyId);
        boolean updateResult = userService.updateById(loginUser);
        if (!updateResult) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户绑定企业失败");
        }
        return companyId;
    }

    @Override
    public Boolean updateCompany(CompanyUpdateRequest companyUpdateRequest) {
        Long id = companyUpdateRequest.getId();
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Company company = this.getById(id);
        if (company == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        String name = companyUpdateRequest.getName();
        if (StringUtils.isBlank(name)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "企业名称不能为空");
        }
        if (!name.equals(company.getName())) {
            Company sameNameCompany = this.getOne(new QueryWrapper<Company>().eq("name", name));
            if (sameNameCompany != null) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "企业名称已经存在");
            }
        }
        String intro = companyUpdateRequest.getIntro();
        if (StringUtils.isBlank(intro)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "企业简介不能为空");
        }
        String address = companyUpdateRequest.getAddress();
        if (StringUtils.isBlank(address)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "企业地址不能为空");
        }
        String type = companyUpdateRequest.getType();
        if (StringUtils.isBlank(type)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "企业类型不能为空");
        }
        String size = companyUpdateRequest.getSize();
        if (StringUtils.isBlank(size)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "企业规模不能为空");
        }
        company.setName(name);
        company.setIntro(intro);
        company.setAddress(address);
        company.setType(type);
        company.setSize(size);
        // 更新后状态变为待审核
        company.setStatus(StatusConstant.PENDING);
        boolean result = this.updateById(company);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return true;
    }

    @Override
    public Boolean uploadLogo(MultipartFile file, HttpServletRequest request) {
        Long companyId = userService.getLoginUser(request).getCompanyId();
        if (companyId == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "当前用户未绑定企业");
        }
        Company company = this.getById(companyId);
        if (company == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "企业不存在");
        }
        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件不能为空");
            }
            // 检查文件类型
            String contentType = file.getContentType();
            if (contentType == null
                    || !(contentType.equals("image/jpeg")
                    || contentType.equals("image/png"))) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "只支持 JPG、PNG文件");
            }
            // 创建上传目录（如果不存在）
            File uploadDir = new File(LOGO_FILE_PATH);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            String fileName = companyId + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(LOGO_FILE_PATH + fileName);
            // 用户是否已上传logo
            if (StringUtils.isBlank(company.getLogo())) {
                // 用户首次上传logo
                Files.copy(file.getInputStream(), filePath);
                company.setLogo(filePath.toString());
                boolean result = this.updateById(company);
                if (!result) {
                    throw new BusinessException(ErrorCode.OPERATION_ERROR, "上传logo失败");
                }
            } else {
                // 用户已上传头像，删除原文件后再上传
                Path oldFilePath = Paths.get(company.getLogo());
                if (Files.exists(oldFilePath)) {
                    try {
                        Files.delete(oldFilePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new BusinessException(ErrorCode.OPERATION_ERROR, "删除原文件失败");
                    }
                }
                Files.copy(file.getInputStream(), filePath);
                company.setLogo(filePath.toString());
                boolean result = this.updateById(company);
                if (!result) {
                    throw new BusinessException(ErrorCode.OPERATION_ERROR, "上传logo失败");
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "上传logo失败");
        }
    }

    @Override
    public Boolean bindCompany(CompanyBindRequest companyBindRequest, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        Long companyId = loginUser.getCompanyId();
        if (companyId != null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "当前用户已经绑定企业");
        }
        String name = companyBindRequest.getName();
        Company company = this.getOne(new QueryWrapper<Company>().eq("name", name));
        if (company == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "企业不存在");
        }
        loginUser.setCompanyId(company.getId());
        boolean result = userService.updateById(loginUser);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户绑定企业失败");
        }
        return true;
    }

    @Override
    public Boolean reviewCompany(Long id) {
        Company company = this.getById(id);
        if (company == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "企业不存在");
        }
        if (Objects.equals(StatusConstant.NORMAL, company.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "企业已经审核通过");
        }
        company.setStatus(StatusConstant.NORMAL);
        boolean result = this.updateById(company);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "企业审核失败");
        }
        return true;
    }

    @Override
    public Boolean freezeCompany(Long id) {
        Company company = this.getById(id);
        if (company == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "企业不存在");
        }
        if (Objects.equals(StatusConstant.DISABLED, company.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "企业已经冻结");
        }
        company.setStatus(StatusConstant.DISABLED);
        boolean result = this.updateById(company);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "企业冻结失败");
        }
        return true;
    }

    @Override
    public Company getRecruiterCompany(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        Long companyId = loginUser.getCompanyId();
        if (companyId == null) {
            return null;
        }
        Company company = this.getById(companyId);
        if (company == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "企业不存在");
        }
        return company;
    }

    @Override
    public QueryWrapper<Company> getCompanyQueryWrapper(CompanyQueryRequest companyQueryRequest) {
        if (companyQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = companyQueryRequest.getId();
        String name = companyQueryRequest.getName();
        String intro = companyQueryRequest.getIntro();
        String address = companyQueryRequest.getAddress();
        String type = companyQueryRequest.getType();
        String size = companyQueryRequest.getSize();
        String logo = companyQueryRequest.getLogo();
        Integer status = companyQueryRequest.getStatus();
        String sortField = companyQueryRequest.getSortField();
        String sortOrder = companyQueryRequest.getSortOrder();
        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.like(StringUtils.isNotBlank(intro), "intro", intro);
        queryWrapper.like(StringUtils.isNotBlank(address), "address", address);
        queryWrapper.like(StringUtils.isNotBlank(type), "type", type);
        queryWrapper.eq(StringUtils.isNotBlank(size), "size", size);
        queryWrapper.eq(StringUtils.isNotBlank(logo), "logo", logo);
        queryWrapper.eq(status != null, "status", status);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }

}