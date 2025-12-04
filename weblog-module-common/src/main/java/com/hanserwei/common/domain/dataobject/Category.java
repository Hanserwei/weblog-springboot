package com.hanserwei.common.domain.dataobject;

import jakarta.persistence.*; // 使用 Jakarta Persistence API (JPA 3.0+)
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import org.hibernate.annotations.*;
import lombok.*; // 引入所有 Lombok 注解

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant; 

/**
 * 文章分类表（t_category 对应实体）
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_category", 
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_name", columnNames = {"name"})
       },
       indexes = {
           @Index(name = "idx_create_time", columnList = "create_time")
       })
@SQLRestriction("is_deleted = false")
public class Category implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分类ID (BIG SERIAL PRIMARY KEY)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 分类名称 (VARCHAR(60) NOT NULL DEFAULT '')
     */
    @Column(name = "name", length = 60, nullable = false)
    private String name = ""; // 对应数据库的 DEFAULT ''

    /**
     * 创建时间 (TIMESTAMP WITHOUT TIME ZONE NOT NULL)
     */
    @CreationTimestamp
    @Column(name = "create_time", nullable = false, updatable = false)
    private Instant createTime;

    /**
     * 最后一次更新时间 (TIMESTAMP WITHOUT TIME ZONE NOT NULL)
     * 配合数据库触发器或 ORM 框架自动更新
     */
    @UpdateTimestamp
    @Column(name = "update_time", nullable = false)
    private Instant updateTime;

    /**
     * 逻辑删除标志位：FALSE：未删除 TRUE：已删除 (BOOLEAN NOT NULL DEFAULT FALSE)
     */
    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}