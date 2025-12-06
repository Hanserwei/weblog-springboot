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
@Table(name = "t_article_category_rel", 
       indexes = {
           @Index(name = "idx_rel_cat_category_id", columnList = "category_id")
       })
public class ArticleCategoryRel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 文章ID (Unique)
     */
    @Column(name = "article_id", nullable = false, unique = true)
    private Long articleId;

    /**
     * 分类ID
     */
    @Column(name = "category_id", nullable = false)
    private Long categoryId;
}