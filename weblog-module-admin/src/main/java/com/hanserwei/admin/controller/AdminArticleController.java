package com.hanserwei.admin.controller;

import com.hanserwei.admin.model.vo.article.*;
import com.hanserwei.admin.service.AdminArticleService;
import com.hanserwei.common.aspect.ApiOperationLog;
import com.hanserwei.common.utils.PageResponse;
import com.hanserwei.common.utils.Response;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端文章控制器
 */
@RestController
@RequestMapping("/admin/article")
public class AdminArticleController {

    @Resource
    private AdminArticleService articleService;

    /**
     * 发布文章
     */
    @PostMapping("/publish")
    @ApiOperationLog(description = "文章发布")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Response<?> publishArticle(@RequestBody @Validated PublishArticleReqVO publishArticleReqVO) {
        return articleService.publishArticle(publishArticleReqVO);
    }

    /**
     * 删除文章
     */
    @PostMapping("/delete")
    @ApiOperationLog(description = "文章删除")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Response<?> deleteArticle(@RequestBody @Validated DeleteArticleReqVO deleteArticleReqVO) {
        return articleService.deleteArticle(deleteArticleReqVO);
    }

    /**
     * 查询文章分页数据
     */
    @PostMapping("/list")
    @ApiOperationLog(description = "查询文章分页数据")
    public PageResponse<FindArticlePageListRspVO> findArticlePageList(@RequestBody @Validated FindArticlePageListReqVO findArticlePageListReqVO) {
        return articleService.findArticlePageList(findArticlePageListReqVO);
    }

    /**
     * 查询文章详情
     */
    @PostMapping("/detail")
    @ApiOperationLog(description = "查询文章详情")
    public Response<FindArticleDetailRspVO> findArticleDetail(@RequestBody @Validated FindArticleDetailReqVO findArticlePageListReqVO) {
        return articleService.findArticleDetail(findArticlePageListReqVO);
    }

    /**
     * 更新文章
     */
    @PostMapping("/update")
    @ApiOperationLog(description = "更新文章")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Response<?> updateArticle(@RequestBody @Validated UpdateArticleReqVO updateArticleReqVO) {
        return articleService.updateArticle(updateArticleReqVO);
    }


}