package com.hanserwei.admin.model.vo.setting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 查询博客设置响应 VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindBlogSettingsRspVO {

    /**
     * 博客 Logo
     */
    private String logo;

    /**
     * 博客名称
     */
    private String name;

    /**
     * 作者名称
     */
    private String author;

    /**
     * 博客介绍
     */
    private String introduction;

    /**
     * 作者头像
     */
    private String avatar;

    /**
     * GitHub 主页地址
     */
    private String githubHomepage;

    /**
     * CSDN 主页地址
     */
    private String csdnHomepage;

    /**
     * Gitee 主页地址
     */
    private String giteeHomepage;

    /**
     * 知乎主页地址
     */
    private String zhihuHomepage;
}