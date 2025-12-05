package com.hanserwei.admin.service;

import com.hanserwei.admin.model.vo.setting.FindBlogSettingsRspVO;
import com.hanserwei.admin.model.vo.setting.UpdateBlogSettingsReqVO;
import com.hanserwei.common.utils.Response;

public interface AdminBlogSettingsService {
    /**
     * 更新博客设置信息
     *
     * @param updateBlogSettingsReqVO 博客设置信息
     * @return 响应
     */
    Response<?> updateBlogSettings(UpdateBlogSettingsReqVO updateBlogSettingsReqVO);

    /**
     * 获取博客设置详情
     *
     * @return 博客设置详情
     */
    Response<FindBlogSettingsRspVO> findDetail();
}