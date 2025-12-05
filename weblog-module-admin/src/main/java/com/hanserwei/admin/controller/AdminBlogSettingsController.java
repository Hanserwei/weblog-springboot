package com.hanserwei.admin.controller;

import com.hanserwei.admin.model.vo.setting.FindBlogSettingsRspVO;
import com.hanserwei.admin.model.vo.setting.UpdateBlogSettingsReqVO;
import com.hanserwei.admin.service.AdminBlogSettingsService;
import com.hanserwei.common.aspect.ApiOperationLog;
import com.hanserwei.common.utils.Response;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端博客设置控制器
 */
@RestController
@RequestMapping("/admin/blog/settings")
public class AdminBlogSettingsController {

    @Resource
    private AdminBlogSettingsService blogSettingsService;

    /**
     * 博客基础信息修改
     */
    @PostMapping("/update")
    @ApiOperationLog(description = "博客基础信息修改")
    public Response<?> updateBlogSettings(@RequestBody @Validated UpdateBlogSettingsReqVO updateBlogSettingsReqVO) {
        return blogSettingsService.updateBlogSettings(updateBlogSettingsReqVO);
    }

    /**
     * 获取博客设置详情
     */
    @PostMapping("/detail")
    @ApiOperationLog(description = "获取博客设置详情")
    public Response<FindBlogSettingsRspVO> findDetail() {
        return blogSettingsService.findDetail();
    }

}