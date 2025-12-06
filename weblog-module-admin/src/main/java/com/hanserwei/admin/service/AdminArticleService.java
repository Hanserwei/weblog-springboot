package com.hanserwei.admin.service;

import com.hanserwei.admin.model.vo.article.*;
import com.hanserwei.common.utils.PageResponse;
import com.hanserwei.common.utils.Response;

public interface AdminArticleService {
    /**
     * 发布文章
     *
     * @param publishArticleReqVO 文章发布参数
     * @return 响应
     */
    Response<?> publishArticle(PublishArticleReqVO publishArticleReqVO);

    /**
     * 删除文章
     *
     * @param deleteArticleReqVO 删除文章参数
     * @return 响应
     */
    Response<?> deleteArticle(DeleteArticleReqVO deleteArticleReqVO);

    /**
     * 查询文章分页数据
     *
     * @param findArticlePageListReqVO 查询文章分页请求参数
     * @return 响应
     */
    PageResponse<FindArticlePageListRspVO> findArticlePageList(FindArticlePageListReqVO findArticlePageListReqVO);

    /**
     * 查询文章详情
     *
     * @param findArticleDetailReqVO 查询文章详情参数
     * @return 响应
     */
    Response<FindArticleDetailRspVO> findArticleDetail(FindArticleDetailReqVO findArticleDetailReqVO);

    /**
     * 更新文章
     *
     * @param updateArticleReqVO 更新文章参数
     * @return 响应
     */
    Response<?> updateArticle(UpdateArticleReqVO updateArticleReqVO);
}