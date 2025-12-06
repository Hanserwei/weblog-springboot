package com.hanserwei.admin.controller;

import com.hanserwei.admin.model.vo.tag.*;
import com.hanserwei.admin.service.AdminTagService;
import com.hanserwei.common.aspect.ApiOperationLog;
import com.hanserwei.common.model.vo.SelectRspVO;
import com.hanserwei.common.utils.PageResponse;
import com.hanserwei.common.utils.Response;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 管理端标签控制器
 */
@RestController
@RequestMapping("/admin")
public class AdminTagController {

    @Resource
    private AdminTagService adminTagService;

    /**
     * 添加标签
     */
    @PostMapping("/tag/add")
    @ApiOperationLog(description = "添加标签")
    public Response<?> addTag(@RequestBody @Validated AddTagReqVO addTagReqVO) {
        return adminTagService.addTag(addTagReqVO);
    }

    /**
     * 标签分页数据获取
     */
    @PostMapping("/tag/list")
    @ApiOperationLog(description = "标签分页数据获取")
    public PageResponse<FindTagPageListRspVO> findTagList(@RequestBody @Validated FindTagPageListReqVO findTagPageListReqVO) {
        return adminTagService.findTagList(findTagPageListReqVO);
    }

    /**
     * 删除标签
     */
    @PostMapping("/tag/delete")
    @ApiOperationLog(description = "删除标签")
    public Response<?> deleteTag(@RequestBody @Validated DeleteTagReqVO deleteTagReqVO) {
        return adminTagService.deleteTag(deleteTagReqVO);
    }

    /**
     * 标签模糊查询
     */
    @PostMapping("/tag/search")
    @ApiOperationLog(description = "标签模糊查询")
    public Response<List<SelectRspVO>> searchTag(@RequestBody @Validated SearchTagReqVO searchTagReqVO) {
        return adminTagService.searchTag(searchTagReqVO);
    }

    /**
     * 根据ID列表获取标签
     */
    @PostMapping("/tag/list/ids")
    @ApiOperationLog(description = "根据ID列表获取标签")
    public Response<List<FindTagsByIdsRspVO>> findTagsByIds(@RequestBody @Validated FindTagsByIdsReqVO findTagsByIdsReqVO) {
        return adminTagService.findTagsByIds(findTagsByIdsReqVO);
    }

}
