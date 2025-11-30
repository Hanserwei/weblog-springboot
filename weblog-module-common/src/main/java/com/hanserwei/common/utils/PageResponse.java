package com.hanserwei.common.utils;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
public class PageResponse<T> extends Response<List<T>> {

    /**
     * 总记录数
     */
    private long total = 0L;

    /**
     * 每页显示的记录数，默认每页显示 10 条
     */
    private long size = 10L;

    /**
     * 当前页码 (JPA Page 从 0 开始, 这里为方便前端, 统一改为从 1 开始)
     */
    private long current;

    /**
     * 总页数
     */
    private long pages;

    /**
     * 成功响应
     *
     * @param page Spring Data JPA 提供的分页接口
     * @param <T> 响应数据类型
     */
    public static <T> PageResponse<T> success(Page<T> page) {
        PageResponse<T> response = new PageResponse<>();
        response.setSuccess(true);

        if (Objects.nonNull(page)) {
            // JPA Page 的 getNumber() 是当前页码 (从 0 开始), 我们在返回时通常习惯改为从 1 开始
            response.setCurrent(page.getNumber() + 1);
            // JPA Page 的 getSize() 是每页大小
            response.setSize(page.getSize());
            // JPA Page 的 getTotalPages() 是总页数
            response.setPages(page.getTotalPages());
            // JPA Page 的 getTotalElements() 是总记录数
            response.setTotal(page.getTotalElements());
            // JPA Page 的 getContent() 是当前页的数据列表
            response.setData(page.getContent());
        } else {
            // 如果传入的 page 为 null，设置默认值
            response.setCurrent(1L);
            response.setSize(10L);
            response.setPages(0L);
            response.setTotal(0L);
            response.setData(null);
        }

        return response;
    }

    // 如果您需要处理分页结果DTO（例如实体转DTO），可以添加一个重载方法：

    /**
     * 成功响应 (适用于将实体 Page<E> 转换为 DTO PageResponse<D>)
     *
     * @param page Spring Data JPA 提供的实体分页接口
     * @param data 经过转换后的 DTO 列表
     * @param <E>  实体类型
     * @param <D>  DTO 类型
     */
    public static <E, D> PageResponse<D> success(Page<E> page, List<D> data) {
        PageResponse<D> response = new PageResponse<>();
        response.setSuccess(true);

        if (Objects.nonNull(page)) {
            response.setCurrent(page.getNumber() + 1);
            response.setSize(page.getSize());
            response.setPages(page.getTotalPages());
            response.setTotal(page.getTotalElements());
            response.setData(data); // 使用传入的 DTO 列表
        } else {
            response.setCurrent(1L);
            response.setSize(10L);
            response.setPages(0L);
            response.setTotal(0L);
            response.setData(data);
        }

        return response;
    }
}