package com.hanserwei.admin.service.impl;

import com.hanserwei.admin.model.vo.AddCategoryReqVO;
import com.hanserwei.admin.model.vo.DeleteCategoryReqVO;
import com.hanserwei.admin.model.vo.FindCategoryPageListReqVO;
import com.hanserwei.admin.model.vo.FindCategoryPageListRspVO;
import com.hanserwei.admin.service.AdminCategoryService;
import com.hanserwei.common.domain.dataobject.Category;
import com.hanserwei.common.domain.repository.CategoryRepository;
import com.hanserwei.common.enums.ResponseCodeEnum;
import com.hanserwei.common.exception.BizException;
import com.hanserwei.common.model.vo.SelectRspVO;
import com.hanserwei.common.utils.PageResponse;
import com.hanserwei.common.utils.Response;
import io.jsonwebtoken.lang.Strings;
import jakarta.annotation.Resource;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AdminCategoryServiceImpl implements AdminCategoryService {

    @Resource
    private CategoryRepository categoryRepository;

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
        Long current = findCategoryPageListReqVO.getCurrent();
        Long size = findCategoryPageListReqVO.getSize();

        Pageable pageable = PageRequest.of(current.intValue() - 1,
                size.intValue(),
                Sort.by(Sort.Direction.DESC, "createTime"));

        // 构建查询条件
        Specification<Category> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            String name = findCategoryPageListReqVO.getName();
            if (Strings.hasText(name)) {
                predicates.add(
                        criteriaBuilder.like(root.get("name"), "%" + name.trim() + "%")
                );
            }
            if (Objects.nonNull(findCategoryPageListReqVO.getStartDate())){
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("createTime"), findCategoryPageListReqVO.getStartDate())
                );
            }
            if (Objects.nonNull(findCategoryPageListReqVO.getEndDate())) {
                predicates.add(
                        criteriaBuilder.lessThan(root.get("createTime"), findCategoryPageListReqVO.getEndDate().plusDays(1).atStartOfDay())
                );
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Category> categoryDOPage = categoryRepository.findAll(specification, pageable);

        List<FindCategoryPageListRspVO> vos = categoryDOPage.getContent().stream()
                .map(category -> FindCategoryPageListRspVO.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .createTime(LocalDateTime.ofInstant(category.getCreateTime(), ZoneId.systemDefault()))
                        .build())
                .collect(Collectors.toList());

        return PageResponse.success(categoryDOPage, vos);
    }

    @Override
    public Response<?> deleteCategory(DeleteCategoryReqVO deleteCategoryReqVO) {
        Long id = deleteCategoryReqVO.getId();
        categoryRepository.deleteById(id);
        return Response.success();
    }

    @Override
    public Response<List<SelectRspVO>> findCategorySelectList() {
        List<Category> categoryList = categoryRepository.findAll();
        // DO 转 VO
        List<SelectRspVO> selectRspVOS = null;
        if (!CollectionUtils.isEmpty(categoryList)){
            selectRspVOS = categoryList.stream()
                    .map(category -> SelectRspVO.builder()
                            .label(category.getName())
                            .value(category.getId())
                            .build())
                    .toList();
        }
        return Response.success(selectRspVOS);
    }
}
