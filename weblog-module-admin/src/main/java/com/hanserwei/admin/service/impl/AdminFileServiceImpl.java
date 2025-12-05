package com.hanserwei.admin.service.impl;

import com.hanserwei.admin.model.vo.file.UploadFileRspVO;
import com.hanserwei.admin.service.AdminFileService;
import com.hanserwei.admin.utils.RustfsUtils;
import com.hanserwei.common.enums.ResponseCodeEnum;
import com.hanserwei.common.exception.BizException;
import com.hanserwei.common.utils.Response;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class AdminFileServiceImpl implements AdminFileService {

    @Resource
    private RustfsUtils rustfsUtils;

    @Override
    public Response<UploadFileRspVO> uploadFile(MultipartFile file) {
        try {
            // 上传文件
            String url = rustfsUtils.uploadFile(file);
            return Response.success(UploadFileRspVO.builder().url(url).build());
        } catch (Exception e) {
            log.error("==> 上传文件异常：{} ...", e.getMessage());
            throw new BizException(ResponseCodeEnum.FILE_UPLOAD_FAILED);
        }
    }
}
