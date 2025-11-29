package com.hanserwei.common.domain.dataobject;

import jakarta.persistence.*; // 使用 Jakarta Persistence API (JPA 3.0+)
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant; 

/**
 * 用户角色表（t_user_role 对应实体）
 */
@Setter
@Getter
@Entity
@Table(name = "t_user_role", 
       indexes = {
           @Index(name = "idx_username", columnList = "username") // 映射数据库中的 idx_username 索引
       })
public class UserRole implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID (BIG SERIAL PRIMARY KEY)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 用户名 (VARCHAR(60) NOT NULL)
     */
    @Column(name = "username", length = 60, nullable = false)
    private String username;

    /**
     * 角色名称 (VARCHAR(60) NOT NULL)
     */
    @Column(name = "role_name", length = 60, nullable = false)
    private String roleName;

    /**
     * 创建时间 (TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP)
     */
    @CreationTimestamp
    @Column(name = "create_time", nullable = false, updatable = false)
    private Instant createTime;

    // --- 构造函数 (Constructor) ---

    public UserRole() {
    }

    public UserRole(String username, String roleName) {
        this.username = username;
        this.roleName = roleName;
    }

}