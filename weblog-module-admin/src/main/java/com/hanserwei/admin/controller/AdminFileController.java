package com.hanserwei.admin.controller;

import com.hanserwei.admin.model.vo.file.UploadFileRspVO;
import com.hanserwei.admin.service.AdminFileService;
import com.hanserwei.common.aspect.ApiOperationLog;
import com.hanserwei.common.utils.Response;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 管理端文件控制器
 */
@RestController
@RequestMapping("/admin")
public class AdminFileController {

    @Resource
    private AdminFileService fileService;

    /**
     * 文件上传
     */
    @PostMapping("/file/upload")
    @ApiOperationLog(description = "文件上传")
    public Response<UploadFileRspVO> uploadFile(@RequestParam("file") MultipartFile file) {
        return fileService.uploadFile(file);
    }

}