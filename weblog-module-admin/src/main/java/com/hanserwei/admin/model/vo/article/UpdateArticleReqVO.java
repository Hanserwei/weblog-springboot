package com.hanserwei.admin.model.vo.article;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;


/**
 * 更新文章请求参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateArticleReqVO {

    /**
     * 文章ID
     */
    @NotNull(message = "文章 ID 不能为空")
    private Long id;

    /**
     * 文章标题
     */
    @NotBlank(message = "文章标题不能为空")
    @Length(min = 1, max = 40, message = "文章标题字数需大于 1 小于 40")
    private String title;

    /**
     * 文章内容
     */
    @NotBlank(message = "文章内容不能为空")
    private String content;

    /**
     * 文章封面图片URL
     */
    @NotBlank(message = "文章封面不能为空")
    private String cover;

    /**
     * 文章摘要
     */
    private String summary;

    /**
     * 文章分类ID
     */
    @NotNull(message = "文章分类不能为空")
    private Long categoryId;

    /**
     * 文章标签列表
     */
    @NotEmpty(message = "文章标签不能为空")
    private List<String> tags;
}
