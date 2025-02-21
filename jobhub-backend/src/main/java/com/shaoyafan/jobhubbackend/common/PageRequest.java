package com.shaoyafan.jobhubbackend.common;

import com.shaoyafan.jobhubbackend.constant.CommonConstant;
import lombok.Data;

/**
 * 分页请求
 *
 * @author SYF
 */
@Data
public class PageRequest {

    /**
     * 当前页号
     */
    private int current = 1;

    /**
     * 页面大小
     */
    private int pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField = "update_time";

    /**
     * 排序顺序（默认降序）
     */
    private String sortOrder = CommonConstant.SORT_ORDER_DESC;
}
