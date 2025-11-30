package com.hanserwei.common.domain.dataobject;

import jakarta.persistence.*; // 使用 Jakarta Persistence API (JPA 3.0+)
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant; // 推荐用于 TIMESTAMP WITH TIME ZONE

/**
 * 用户表（t_user 对应实体）
 */
@Entity
@Setter
@Getter
@Builder
@Table(name = "t_user")
@SQLRestriction("is_deleted = false")
@SQLDelete(sql = "UPDATE t_user SET is_deleted = true WHERE id = ?")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID (BIG SERIAL)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 用户名 (VARCHAR(60) NOT NULL UNIQUE)
     */
    @Column(name = "username", length = 60, nullable = false, unique = true)
    private String username;

    /**
     * 密码 (VARCHAR(60) NOT NULL)
     */
    @Column(name = "password", length = 60, nullable = false)
    private String password;

    /**
     * 创建时间 (TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW())
     * 使用 Hibernate 的 @CreationTimestamp 确保创建时自动赋值
     */
    @CreationTimestamp
    @Column(name = "create_time", nullable = false, updatable = false)
    private Instant createTime;

    /**
     * 最后一次更新时间 (TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW())
     * 使用 Hibernate 的 @UpdateTimestamp 确保更新时自动赋值
     * 注意：虽然数据库有触发器，但使用此注解可保持 ORM 层的同步性
     */
    @UpdateTimestamp
    @Column(name = "update_time", nullable = false)
    private Instant updateTime;

    /**
     * 逻辑删除：FALSE：未删除 TRUE：已删除 (BOOLEAN NOT NULL DEFAULT FALSE)
     */
    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false; // 对应数据库默认值

    public User() {
    }

    public User(Long id, String username, String password, Instant createTime, Instant updateTime, Boolean isDeleted) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.isDeleted = isDeleted;
    }
}
