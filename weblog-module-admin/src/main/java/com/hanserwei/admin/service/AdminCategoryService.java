package com.hanserwei.admin.service;

import com.hanserwei.admin.model.vo.category.AddCategoryReqVO;
import com.hanserwei.admin.model.vo.category.DeleteCategoryReqVO;
import com.hanserwei.admin.model.vo.category.FindCategoryPageListReqVO;
import com.hanserwei.admin.model.vo.category.FindCategoryPageListRspVO;
import com.hanserwei.common.model.vo.SelectRspVO;
import com.hanserwei.common.utils.PageResponse;
import com.hanserwei.common.utils.Response;

import java.util.List;

public interface AdminCategoryService {
    /**
     * 添加分类
     *
     * @param addCategoryReqVO 添加分类请求参数
     * @return 添加结果
     */
    Response<?> addCategory(AddCategoryReqVO addCategoryReqVO);

    /**
     * 分类分页数据查询
     *
     * @param findCategoryPageListReqVO 分页查询分类参数
     * @return 查询结果
     */
    PageResponse<FindCategoryPageListRspVO> findCategoryList(FindCategoryPageListReqVO findCategoryPageListReqVO);

    /**
     * 删除分类
     *
     * @param deleteCategoryReqVO 删除分类参数
     * @return 删除结果
     */
    Response<?> deleteCategory(DeleteCategoryReqVO deleteCategoryReqVO);

    /**
     * 获取文章分类的 Select 列表数据
     *
     * @return Select 列表数据
     */
    Response<List<SelectRspVO>>  findCategorySelectList();

}
