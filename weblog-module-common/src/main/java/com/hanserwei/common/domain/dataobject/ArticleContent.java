package com.hanserwei.common.domain.dataobject;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_article_content", indexes = {
    @Index(name = "idx_article_content_article_id", columnList = "article_id")
})
public class ArticleContent implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联 Article 的 ID
     * 这里只存储 ID，不建立对象关联，以实现轻量级操作
     */
    @Column(name = "article_id", nullable = false)
    private Long articleId;

    /**
     * 正文内容
     * 映射为 TEXT 类型，支持大文本
     */
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;
}