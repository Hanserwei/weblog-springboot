package com.hanserwei.common.utils;

import com.hanserwei.common.model.BasePageQuery;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * 分页查询工具类
 * 用于抽取通用的分页查询逻辑
 */
public class PageHelper {

    /**
     * 执行带条件的分页查询
     *
     * @param repository  JPA Repository
     * @param pageQuery   分页查询参数
     * @param searchText  搜索文本（用于模糊查询）
     * @param searchField 搜索字段名称（如 "name"、"title" 等）
     * @param startDate   开始日期
     * @param endDate     结束日期
     * @param converter   DO 到 VO 的转换函数
     * @param <T>         实体类型
     * @param <R>         响应VO类型
     * @return 分页响应
     */
    public static <T, R> PageResponse<R> findPageList(
            JpaSpecificationExecutor<T> repository,
            BasePageQuery pageQuery,
            String searchText,
            String searchField,
            LocalDate startDate,
            LocalDate endDate,
            Function<T, R> converter) {

        // 构建分页参数
        Pageable pageable = PageRequest.of(
                pageQuery.getCurrent().intValue() - 1,
                pageQuery.getSize().intValue(),
                Sort.by(Sort.Direction.DESC, "createTime")
        );

        // 构建查询条件
        Specification<T> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 搜索文本模糊查询
            if (StringUtils.hasText(searchText) && StringUtils.hasText(searchField)) {
                predicates.add(
                        criteriaBuilder.like(root.get(searchField), "%" + searchText.trim() + "%")
                );
            }

            // 开始日期查询
            if (Objects.nonNull(startDate)) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("createTime"), startDate)
                );
            }

            // 结束日期查询
            if (Objects.nonNull(endDate)) {
                predicates.add(
                        criteriaBuilder.lessThan(root.get("createTime"), endDate.plusDays(1).atStartOfDay())
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        // 执行查询
        Page<T> page = repository.findAll(specification, pageable);

        // DO 转 VO
        List<R> vos = page.getContent().stream()
                .map(converter)
                .toList();

        return PageResponse.success(page, vos);
    }

    /**
     * 执行带条件的分页查询（使用默认字段名 "name"）
     *
     * @param repository JPA Repository
     * @param pageQuery  分页查询参数
     * @param name       名称（用于模糊查询）
     * @param startDate  开始日期
     * @param endDate    结束日期
     * @param converter  DO 到 VO 的转换函数
     * @param <T>        实体类型
     * @param <R>        响应VO类型
     * @return 分页响应
     */
    public static <T, R> PageResponse<R> findPageList(
            JpaSpecificationExecutor<T> repository,
            BasePageQuery pageQuery,
            String name,
            LocalDate startDate,
            LocalDate endDate,
            Function<T, R> converter) {
        return findPageList(repository, pageQuery, name, "name", startDate, endDate, converter);
    }
}
