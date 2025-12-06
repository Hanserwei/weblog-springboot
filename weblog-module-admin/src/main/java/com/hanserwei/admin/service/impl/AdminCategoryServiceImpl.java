package com.hanserwei.admin.service.impl;

import com.hanserwei.admin.model.vo.category.*;
import com.hanserwei.admin.service.AdminCategoryService;
import com.hanserwei.common.domain.dataobject.ArticleCategoryRel;
import com.hanserwei.common.domain.dataobject.Category;
import com.hanserwei.common.domain.repository.ArticleCategoryRelRepository;
import com.hanserwei.common.domain.repository.CategoryRepository;
import com.hanserwei.common.enums.ResponseCodeEnum;
import com.hanserwei.common.exception.BizException;
import com.hanserwei.common.model.vo.SelectRspVO;
import com.hanserwei.common.utils.PageHelper;
import com.hanserwei.common.utils.PageResponse;
import com.hanserwei.common.utils.Response;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class AdminCategoryServiceImpl implements AdminCategoryService {

    @Resource
    private CategoryRepository categoryRepository;
    @Resource
    private ArticleCategoryRelRepository articleCategoryRelRepository;

    @Override
    public Response<?> addCategory(AddCategoryReqVO addCategoryReqVO) {
        String categoryName = addCategoryReqVO.getName();
        // 先判断是否存在
        if (categoryRepository.existsCategoryByName(categoryName)) {
            throw new BizException(ResponseCodeEnum.CATEGORY_NAME_IS_EXISTED);
        }
        // 构造Category对象
        Category category = Category.builder()
                .name(categoryName)
                .build();
        categoryRepository.save(category);
        return Response.success();
    }

    @Override
    public PageResponse<FindCategoryPageListRspVO> findCategoryList(FindCategoryPageListReqVO findCategoryPageListReqVO) {
        return PageHelper.findPageList(
                categoryRepository,
                findCategoryPageListReqVO,
                findCategoryPageListReqVO.getName(),
                findCategoryPageListReqVO.getStartDate(),
                findCategoryPageListReqVO.getEndDate(),
                category -> FindCategoryPageListRspVO.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .createTime(LocalDateTime.ofInstant(category.getCreateTime(), ZoneId.systemDefault()))
                        .build()
        );
    }

    @Override
    public Response<?> deleteCategory(DeleteCategoryReqVO deleteCategoryReqVO) {
        List<ArticleCategoryRel> articleTagRelList = articleCategoryRelRepository.findByCategoryId((deleteCategoryReqVO.getId()));
        if (!CollectionUtils.isEmpty(articleTagRelList)) {
            throw new BizException(ResponseCodeEnum.CATEGORY_HAS_ARTICLE);
        }
        return categoryRepository.findById(deleteCategoryReqVO.getId())
                .map(tag -> {
                    tag.setIsDeleted(true);
                    categoryRepository.save(tag);
                    return Response.success();
                })
                .orElse(Response.fail(ResponseCodeEnum.CATEGORY_NOT_EXIST));
    }

    @Override
    public Response<List<SelectRspVO>> findCategorySelectList() {
        List<Category> categoryList = categoryRepository.findAll();
        // DO 转 VO
        List<SelectRspVO> selectRspVOS = null;
        if (!CollectionUtils.isEmpty(categoryList)) {
            selectRspVOS = categoryList.stream()
                    .map(category -> SelectRspVO.builder()
                            .label(category.getName())
                            .value(category.getId())
                            .build())
                    .toList();
        }
        return Response.success(selectRspVOS);
    }

    @Override
    public Response<FindCategoryByIdRspVO> findCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new BizException(ResponseCodeEnum.CATEGORY_NOT_EXIST));
        return Response.success(FindCategoryByIdRspVO.builder()
                .id(category.getId())
                .name(category.getName())
                .build());
    }
}
