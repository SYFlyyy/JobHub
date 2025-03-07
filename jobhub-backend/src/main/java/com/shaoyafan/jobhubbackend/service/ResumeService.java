package com.shaoyafan.jobhubbackend.service;

import com.shaoyafan.jobhubbackend.model.domain.Resume;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shaoyafan.jobhubbackend.model.dto.resume.ResumeIdRequest;
import com.shaoyafan.jobhubbackend.model.dto.user.UserIdRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 简历附件服务
 *
* @author SYF
* @description 针对表【resume(简历附件表)】的数据库操作Service
* @createDate 2025-01-26 16:39:45
*/
public interface ResumeService extends IService<Resume> {

    /**
     * 上传简历附件
     *
     * @param file
     * @param request
     * @return
     */
    Boolean uploadResume(MultipartFile file, Integer slot, HttpServletRequest request);

    /**
     * 下载求职者简历附件
     *
     * @param userIdRequest
     * @return
     */
    ResponseEntity<Resource> downloadResume(UserIdRequest userIdRequest);

    /**
     * 逻辑删除简历附件
     *
     * @param request
     * @return
     */
    Boolean deleteResume(HttpServletRequest request);

    /**
     * 获取简历附件路径
     *
     * @param request
     * @return
     */
    List<Resume> getUserResumePath(HttpServletRequest request);

    /**
     * 招聘者获取简历附件路径
     *
     * @param resumeIdRequest
     * @return
     */
    String getResumePath(ResumeIdRequest resumeIdRequest);
}
