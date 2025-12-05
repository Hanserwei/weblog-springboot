package com.hanserwei.admin.service.impl;

import com.hanserwei.admin.model.vo.setting.FindBlogSettingsRspVO;
import com.hanserwei.admin.model.vo.setting.UpdateBlogSettingsReqVO;
import com.hanserwei.admin.service.AdminBlogSettingsService;
import com.hanserwei.common.domain.dataobject.BlogSettings;
import com.hanserwei.common.domain.repository.BlogSettingsRepository;
import com.hanserwei.common.utils.Response;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


@Service
public class AdminBlogSettingsServiceImpl implements AdminBlogSettingsService {

    @Resource
    private BlogSettingsRepository blogSettingsRepository;


    @Override
    public Response<?> updateBlogSettings(UpdateBlogSettingsReqVO updateBlogSettingsReqVO) {
        // 保存或更新博客设置
        blogSettingsRepository.findById(1L)
                .ifPresentOrElse(existingSettings -> {
                    // 如果存在，则更新现有记录
                    BeanUtils.copyProperties(updateBlogSettingsReqVO, existingSettings);
                    blogSettingsRepository.saveAndFlush(existingSettings);
                }, () -> {
                    // 如果不存在，则创建新记录
                    BlogSettings blogSettings = new BlogSettings();
                    BeanUtils.copyProperties(updateBlogSettingsReqVO, blogSettings);
                    blogSettings.setId(1L);
                    blogSettingsRepository.saveAndFlush(blogSettings);
                });
        return Response.success();
    }


    @Override
    public Response<FindBlogSettingsRspVO> findDetail() {
        return blogSettingsRepository.findById(1L)
                .map(e -> {
                    FindBlogSettingsRspVO findBlogSettingsRspVO = new FindBlogSettingsRspVO();
                    BeanUtils.copyProperties(e, findBlogSettingsRspVO);
                    return Response.success(findBlogSettingsRspVO);
                })
                .orElse(Response.success(null));
    }
}
