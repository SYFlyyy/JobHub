package com.shaoyafan.jobhubbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shaoyafan.jobhubbackend.common.ErrorCode;
import com.shaoyafan.jobhubbackend.constant.StatusConstant;
import com.shaoyafan.jobhubbackend.exception.BusinessException;
import com.shaoyafan.jobhubbackend.model.domain.Resume;
import com.shaoyafan.jobhubbackend.model.domain.User;
import com.shaoyafan.jobhubbackend.model.dto.user.UserIdRequest;
import com.shaoyafan.jobhubbackend.service.ResumeService;
import com.shaoyafan.jobhubbackend.mapper.ResumeMapper;
import com.shaoyafan.jobhubbackend.service.UserService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

/**
* @author SYF
* @description 针对表【resume(简历附件表)】的数据库操作Service实现
* @createDate 2025-01-26 16:39:45
*/
@Service
public class ResumeServiceImpl extends ServiceImpl<ResumeMapper, Resume>
    implements ResumeService {

    @Resource
    private UserService userService;

    private static final String RESUME_FILE_PATH = "resume/";

    @Override
    @Transactional
    public Boolean uploadResume(MultipartFile file, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件不能为空");
            }
            // 检查文件类型
            String contentType = file.getContentType();
            if (contentType == null
                    || !(contentType.equals("image/jpeg")
                    || contentType.equals("image/png")
                    || contentType.equals("application/pdf"))) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "只支持 JPG、PNG 和 PDF 文件");
            }
            // 创建上传目录（如果不存在）
            File uploadDir = new File(RESUME_FILE_PATH);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            String fileName = userId + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(RESUME_FILE_PATH + fileName);
            Resume existResume = this.getOne(new QueryWrapper<Resume>().eq("user_id", userId));
            // 用户是否已有简历附件
            if (existResume == null) {
                // 用户首次上传简历附件
                Files.copy(file.getInputStream(), filePath);
                // 保存文件信息到数据库
                Resume resume = new Resume();
                resume.setUserId(userId);
                resume.setFileName(fileName);
                resume.setFilePath(filePath.toString());
                resume.setFileType(contentType);
                // 默认为正常状态
                resume.setStatus(StatusConstant.NORMAL);
                boolean result = this.save(resume);
                if (!result) {
                    throw new BusinessException(ErrorCode.OPERATION_ERROR, "上传文件失败");
                }
            } else {
                // 用户已有简历附件，删除原文件后再上传
                Path oldFilePath = Paths.get(existResume.getFilePath());
                if (Files.exists(oldFilePath)) {
                    try {
                        Files.delete(oldFilePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new BusinessException(ErrorCode.OPERATION_ERROR, "删除原文件失败");
                    }
                }
                Files.copy(file.getInputStream(), filePath);
                existResume.setFileName(fileName);
                existResume.setFilePath(filePath.toString());
                existResume.setFileType(contentType);
                boolean result = this.updateById(existResume);
                if (!result) {
                    throw new BusinessException(ErrorCode.OPERATION_ERROR, "上传文件失败");
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "上传文件失败");
        }
    }

    @Override
    public ResponseEntity<org.springframework.core.io.Resource> downloadResume(UserIdRequest userIdRequest) {
        Long userId = userIdRequest.getId();
        Resume resume = this.getOne(new QueryWrapper<Resume>().eq("user_id", userId));
        if (resume == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户未上传简历附件");
        }
        try {
            // 构建文件路径
            String filePath = resume.getFilePath();
            File file = new File(filePath);
            // 检查文件是否存在
            if (!file.exists()) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "文件不存在");
            }
            // 获取文件名
            String fileName = resume.getFileName();
            // 查找字符串中最后一个下划线 _ 的索引位置
            int underscoreIndex = fileName.lastIndexOf("_");
            if (underscoreIndex != -1 && underscoreIndex < fileName.length() - 1) {
                fileName = fileName.substring(underscoreIndex + 1);
            }
            // 创建资源对象
            org.springframework.core.io.Resource resource = new FileSystemResource(file);
            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));            // 返回响应实体
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "下载文件失败");
        }
    }
}




