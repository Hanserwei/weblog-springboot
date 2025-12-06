package com.hanserwei.admin.service;

import com.hanserwei.admin.model.vo.tag.*;
import com.hanserwei.common.model.vo.SelectRspVO;
import com.hanserwei.common.utils.PageResponse;
import com.hanserwei.common.utils.Response;

import java.util.List;

public interface AdminTagService {
    /**
     * 添加标签
     *
     * @param addTagReqVO 添加标签请求参数
     * @return 响应结果
     */
    Response<?> addTag(AddTagReqVO addTagReqVO);

    /**
     * 标签分页数据获取
     *
     * @param findTagPageListReqVO 标签分页数据获取请求参数
     * @return 响应结果
     */
    PageResponse<FindTagPageListRspVO> findTagList(FindTagPageListReqVO findTagPageListReqVO);

    /**
     * 删除标签
     *
     * @param deleteTagReqVO 删除标签请求参数
     * @return 响应结果
     */
    Response<?> deleteTag(DeleteTagReqVO deleteTagReqVO);

    /**
     * 标签下拉列表数据获取
     *
     * @param searchTagReqVO 搜索标签请求参数
     * @return 响应结果
     */
    Response<List<SelectRspVO>> searchTag(SearchTagReqVO searchTagReqVO);

    /**
     * 根据 ID 列表获取标签
     *
     * @param findTagsByIdsReqVO 根据 ID 列表获取标签请求参数
     * @return 响应结果
     */
    Response<List<FindTagsByIdsRspVO>> findTagsByIds(FindTagsByIdsReqVO findTagsByIdsReqVO);
}
