package com.shaoyafan.jobhubbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shaoyafan.jobhubbackend.model.domain.Notice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shaoyafan.jobhubbackend.model.dto.notice.NoticeAddRequest;
import com.shaoyafan.jobhubbackend.model.dto.notice.NoticeIdRequest;
import com.shaoyafan.jobhubbackend.model.dto.notice.NoticeQueryRequest;
import com.shaoyafan.jobhubbackend.model.dto.notice.NoticeUpdateRequest;

/**
 * 公告服务
 *
* @author SYF
* @description 针对表【notice(公告表)】的数据库操作Service
* @createDate 2025-01-26 16:39:45
*/
public interface NoticeService extends IService<Notice> {

    /**
     * 添加公告
     *
     * @param noticeAddRequest
     * @return
     */
    Long addNotice(NoticeAddRequest noticeAddRequest);

    /**
     * 根据id获取公告
     *
     * @param noticeIdRequest
     * @return
     */
    Notice getNoticeById(NoticeIdRequest noticeIdRequest);

    /**
     * 更新公告
     *
     * @param noticeUpdateRequest
     * @return
     */
    Boolean updateNotice(NoticeUpdateRequest noticeUpdateRequest);

    /**
     * 上线公告
     *
     * @param id
     * @return
     */
    Boolean onlineNotice(Long id);

    /**
     * 下线公告
     *
     * @param id
     * @return
     */
    Boolean offlineNotice(Long id);

    /**
     * 获取查询条件
     *
     * @param noticeQueryRequest
     * @return
     */
    QueryWrapper<Notice> getNoticeQueryWrapper(NoticeQueryRequest noticeQueryRequest);
}
