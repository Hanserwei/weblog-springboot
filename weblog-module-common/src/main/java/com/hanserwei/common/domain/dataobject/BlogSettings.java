package com.hanserwei.common.domain.dataobject;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * 博客设置表
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_blog_settings")
public class BlogSettings implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 博客Logo
     * 数据库类型为 TEXT，Java中映射为 String 即可
     */
    @Column(name = "logo", nullable = false, columnDefinition = "TEXT")
    @Builder.Default
    private String logo = "";

    /**
     * 博客名称
     */
    @Column(name = "name", length = 60, nullable = false)
    @Builder.Default
    private String name = "";

    /**
     * 作者名
     */
    @Column(name = "author", length = 20, nullable = false)
    @Builder.Default
    private String author = "";

    /**
     * 介绍语
     */
    @Column(name = "introduction", nullable = false, columnDefinition = "TEXT")
    @Builder.Default
    private String introduction = "";

    /**
     * 作者头像
     */
    @Column(name = "avatar", nullable = false, columnDefinition = "TEXT")
    @Builder.Default
    private String avatar = "";

    /**
     * GitHub 主页访问地址
     */
    @Column(name = "github_homepage", nullable = false, columnDefinition = "TEXT")
    @Builder.Default
    private String githubHomepage = "";

    /**
     * CSDN 主页访问地址
     */
    @Column(name = "csdn_homepage", nullable = false, columnDefinition = "TEXT")
    @Builder.Default
    private String csdnHomepage = "";

    /**
     * Gitee 主页访问地址
     */
    @Column(name = "gitee_homepage", nullable = false, columnDefinition = "TEXT")
    @Builder.Default
    private String giteeHomepage = "";

    /**
     * 知乎主页访问地址
     */
    @Column(name = "zhihu_homepage", nullable = false, columnDefinition = "TEXT")
    @Builder.Default
    private String zhihuHomepage = "";
}