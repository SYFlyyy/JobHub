package com.shaoyafan.jobhubbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shaoyafan.jobhubbackend.common.ErrorCode;
import com.shaoyafan.jobhubbackend.constant.CommonConstant;
import com.shaoyafan.jobhubbackend.constant.StatusConstant;
import com.shaoyafan.jobhubbackend.exception.BusinessException;
import com.shaoyafan.jobhubbackend.model.domain.Notice;
import com.shaoyafan.jobhubbackend.model.dto.notice.NoticeAddRequest;
import com.shaoyafan.jobhubbackend.model.dto.notice.NoticeIdRequest;
import com.shaoyafan.jobhubbackend.model.dto.notice.NoticeQueryRequest;
import com.shaoyafan.jobhubbackend.model.dto.notice.NoticeUpdateRequest;
import com.shaoyafan.jobhubbackend.service.NoticeService;
import com.shaoyafan.jobhubbackend.mapper.NoticeMapper;
import com.shaoyafan.jobhubbackend.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
* @author SYF
* @description 针对表【notice(公告表)】的数据库操作Service实现
* @createDate 2025-01-26 16:39:45
*/
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice>
    implements NoticeService {

    @Override
    public Long addNotice(NoticeAddRequest noticeAddRequest) {
        String title = noticeAddRequest.getTitle();
        String content = noticeAddRequest.getContent();
        if (StringUtils.isAnyBlank(title, content)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题或内容不能为空");
        }
        Notice notice = new Notice();
        notice.setTitle(title);
        notice.setContent(content);
        notice.setStatus(StatusConstant.NORMAL);
        boolean result = this.save(notice);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return notice.getId();
    }

    @Override
    public Notice getNoticeById(NoticeIdRequest noticeIdRequest) {
        Long id = noticeIdRequest.getId();
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Notice notice = this.getById(id);
        if (notice == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return notice;
    }

    @Override
    public Boolean updateNotice(NoticeUpdateRequest noticeUpdateRequest) {
        Long id = noticeUpdateRequest.getId();
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Notice notice = this.getById(id);
        if (notice == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        String title = noticeUpdateRequest.getTitle();
        String content = noticeUpdateRequest.getContent();
        if (StringUtils.isAnyBlank(title, content)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题或内容不能为空");
        }
        notice.setTitle(title);
        notice.setContent(content);
        boolean result = this.updateById(notice);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return true;
    }

    @Override
    public Boolean onlineNotice(Long id) {
        Notice notice = this.getById(id);
        if (notice == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if (Objects.equals(notice.getStatus(), StatusConstant.NORMAL)) {
            throw new BusinessException(ErrorCode.STATE_ERROR, "公告已上线");
        }
        // 上线
        notice.setStatus(StatusConstant.NORMAL);
        boolean result = this.updateById(notice);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return true;
    }

    @Override
    public Boolean offlineNotice(Long id) {
        Notice notice = this.getById(id);
        if (notice == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if (Objects.equals(notice.getStatus(), StatusConstant.DISABLED)) {
            throw new BusinessException(ErrorCode.STATE_ERROR, "公告已下线");
        }
        // 下线
        notice.setStatus(StatusConstant.DISABLED);
        boolean result = this.updateById(notice);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return true;
    }

    @Override
    public QueryWrapper<Notice> getNoticeQueryWrapper(NoticeQueryRequest noticeQueryRequest) {
        if (noticeQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = noticeQueryRequest.getId();
        String title = noticeQueryRequest.getTitle();
        String content = noticeQueryRequest.getContent();
        Integer status = noticeQueryRequest.getStatus();
        String sortField = noticeQueryRequest.getSortField();
        String sortOrder = noticeQueryRequest.getSortOrder();
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.eq(status != null, "status", status);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }

}




