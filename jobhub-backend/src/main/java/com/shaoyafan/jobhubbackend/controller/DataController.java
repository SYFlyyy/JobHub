package com.shaoyafan.jobhubbackend.controller;

import com.shaoyafan.jobhubbackend.annotation.AuthCheck;
import com.shaoyafan.jobhubbackend.common.BaseResponse;
import com.shaoyafan.jobhubbackend.model.domain.PlatformData;
import com.shaoyafan.jobhubbackend.model.dto.job.JobIdRequest;
import com.shaoyafan.jobhubbackend.model.vo.hiringData.HiringDataVO;
import com.shaoyafan.jobhubbackend.service.HiringDataService;
import com.shaoyafan.jobhubbackend.service.PlatformDataService;
import com.shaoyafan.jobhubbackend.utils.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 数据分析接口
 *
 * @author SYF
 */
@RestController
@RequestMapping("/data")
@Slf4j
@Api(tags = "数据分析接口")
public class DataController {

    @Resource
    private PlatformDataService platformDataService;

    @Resource
    private HiringDataService hiringDataService;

    /**
     * 获取平台数据
     *
     * @return
     */
    @GetMapping("/platform")
    @AuthCheck(mustRole = 0)
    @ApiOperation("获取平台数据")
    public BaseResponse<PlatformData> getPlatformData() {
        PlatformData platformData = platformDataService.getPlatformData();
        return ResultUtils.success(platformData);
    }

    /**
     * 获取招聘数据
     *
     * @param jobIdRequest
     * @return
     */
    @PostMapping("/hiring")
    @AuthCheck(mustRole = 2)
    @ApiOperation("获取招聘数据")
    public BaseResponse<HiringDataVO> getHiringData(@RequestBody JobIdRequest jobIdRequest) {
        HiringDataVO hiringDataVO = hiringDataService.getHiringData(jobIdRequest);
        return ResultUtils.success(hiringDataVO);
    }
}
