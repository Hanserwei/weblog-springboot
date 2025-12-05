package com.hanserwei.admin.model.vo.setting;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 更新博客设置请求 VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateBlogSettingsReqVO {

    /**
     * 博客 LOGO
     */
    @NotBlank(message = "博客 LOGO 不能为空")
    private String logo;

    /**
     * 博客名称
     */
    @NotBlank(message = "博客名称不能为空")
    private String name;

    /**
     * 博客作者
     */
    @NotBlank(message = "博客作者不能为空")
    private String author;

    /**
     * 博客介绍语
     */
    @NotBlank(message = "博客介绍语不能为空")
    private String introduction;

    /**
     * 博客头像
     */
    @NotBlank(message = "博客头像不能为空")
    private String avatar;

    /**
     * GitHub 主页
     */
    private String githubHomepage;

    /**
     * CSDN 主页
     */
    private String csdnHomepage;

    /**
     * Gitee 主页
     */
    private String giteeHomepage;

    /**
     * 知乎主页
     */
    private String zhihuHomepage;
}
