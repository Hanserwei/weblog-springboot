package com.hanserwei.common.domain.dataobject;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_article", indexes = {
        @Index(name = "idx_article_create_time", columnList = "create_time")
})
@SQLRestriction("is_deleted = false")
@SQLDelete(sql = "update t_article set is_deleted = true where id = ?")
public class Article implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Builder.Default
    private String cover = "";

    @Column(columnDefinition = "TEXT")
    @Builder.Default
    private String summary = "";

    @Column(name = "read_num", nullable = false)
    @Builder.Default
    private Integer readNum = 1;

    @CreationTimestamp
    @Column(name = "create_time", nullable = false, updatable = false)
    private Instant createTime;

    @UpdateTimestamp
    @Column(name = "update_time", nullable = false)
    private Instant updateTime;

    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;
}