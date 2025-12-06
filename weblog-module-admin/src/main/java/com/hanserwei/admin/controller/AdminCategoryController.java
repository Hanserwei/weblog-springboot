package com.hanserwei.admin.controller;

import com.hanserwei.admin.model.vo.category.*;
import com.hanserwei.admin.service.AdminCategoryService;
import com.hanserwei.common.aspect.ApiOperationLog;
import com.hanserwei.common.model.vo.SelectRspVO;
import com.hanserwei.common.utils.PageResponse;
import com.hanserwei.common.utils.Response;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理端分类控制器
 */
@RestController
@RequestMapping("/admin")
public class AdminCategoryController {

    @Resource
    private AdminCategoryService adminCategoryService;

    /**
     * 添加分类
     */
    @PostMapping("/category/add")
    @ApiOperationLog(description = "添加分类")
    public Response<?> addCategory(@RequestBody @Validated AddCategoryReqVO addCategoryReqVO) {
        return adminCategoryService.addCategory(addCategoryReqVO);
    }

    /**
     * 分类分页数据获取
     */
    @PostMapping("/category/list")
    @ApiOperationLog(description = "分类分页数据获取")
    public PageResponse<FindCategoryPageListRspVO> findCategoryList(@RequestBody @Validated FindCategoryPageListReqVO findCategoryPageListReqVO) {
        return adminCategoryService.findCategoryList(findCategoryPageListReqVO);
    }

    /**
     * 删除分类
     */
    @PostMapping("/category/delete")
    @ApiOperationLog(description = "删除分类")
    public Response<?> deleteCategory(@RequestBody @Validated DeleteCategoryReqVO deleteCategoryReqVO) {
        return adminCategoryService.deleteCategory(deleteCategoryReqVO);
    }

    /**
     * 分类下拉列表
     */
    @PostMapping("/category/select/list")
    @ApiOperationLog(description = "分类 Select 下拉列表数据获取")
    public Response<List<SelectRspVO>> findCategorySelectList() {
        return adminCategoryService.findCategorySelectList();
    }

    /**
     * 根据分类ID查询
     */
    @GetMapping("/category/find/{id}")
    @ApiOperationLog(description = "根据分类ID查询")
    public Response<FindCategoryByIdRspVO> findCategoryById(@PathVariable Long id) {
        return adminCategoryService.findCategoryById(id);
    }

}
