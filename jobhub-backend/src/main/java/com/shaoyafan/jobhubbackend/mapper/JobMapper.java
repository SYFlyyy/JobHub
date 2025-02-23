package com.shaoyafan.jobhubbackend.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shaoyafan.jobhubbackend.model.domain.Job;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shaoyafan.jobhubbackend.model.vo.job.JobVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.sql.Wrapper;
import java.util.List;

/**
* @author SYF
* @description 针对表【job(职位表)】的数据库操作Mapper
* @createDate 2025-01-26 16:39:45
* @Entity generator.domain.Job
*/
public interface JobMapper extends BaseMapper<Job> {

    /**
     * 分页联表查询职位列表
     *
     * @param page
     * @param QueryWrapper
     * @return
     */
    @Select("SELECT job.*, company.name as companyName, company.size, company.address FROM job LEFT JOIN company ON job.company_id = company.id WHERE ${ew.sqlSegment}")
    IPage<JobVO> selectJobWithCompany(Page<JobVO> page, @Param("ew") QueryWrapper<Job> QueryWrapper);

}




