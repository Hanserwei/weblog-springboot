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
@Table(name = "t_article_tag_rel", 
       indexes = {
           @Index(name = "idx_rel_tag_article_id", columnList = "article_id"),
           @Index(name = "idx_rel_tag_tag_id", columnList = "tag_id")
       },
       uniqueConstraints = {
           // 对应数据库中的联合唯一约束
           @UniqueConstraint(name = "uk_article_tag_rel_unique_pair", columnNames = {"article_id", "tag_id"})
       })
public class ArticleTagRel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 文章ID
     */
    @Column(name = "article_id", nullable = false)
    private Long articleId;

    /**
     * 标签ID
     */
    @Column(name = "tag_id", nullable = false)
    private Long tagId;
}