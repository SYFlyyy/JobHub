package com.shaoyafan.jobhubbackend.mapper;

import com.shaoyafan.jobhubbackend.model.domain.JobCollection;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author SYF
* @description 针对表【job_collection(职位收藏表)】的数据库操作Mapper
* @createDate 2025-01-26 16:39:45
* @Entity generator.domain.JobCollection
*/
public interface JobCollectionMapper extends BaseMapper<JobCollection> {

    /**
     * 根据用户id获取职位id
     *
     * @param
     * @return
     */
    @Select("SELECT job_id FROM job_collection WHERE user_id = #{user_id} AND status = 0")
    List<Long> getJobIdByUserId(@Param("user_id") Long userId);

}




