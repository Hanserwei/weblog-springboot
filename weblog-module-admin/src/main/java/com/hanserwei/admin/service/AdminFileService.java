package com.hanserwei.admin.service;

import com.hanserwei.admin.model.vo.file.UploadFileRspVO;
import com.hanserwei.common.utils.Response;
import org.springframework.web.multipart.MultipartFile;

public interface AdminFileService {
    /**
     * 上传文件
     *
     * @param file 文件
     * @return 访问地址
     */
    Response<UploadFileRspVO> uploadFile(MultipartFile file);
}