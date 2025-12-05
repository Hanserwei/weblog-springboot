
package com.hanserwei.admin.utils;

import com.hanserwei.common.config.RustfsProperties;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;

@Component
@Slf4j
public class RustfsUtils {

    @Resource
    private RustfsProperties rustfsProperties;

    @Resource
    private S3Client s3Client;

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 访问地址
     * @throws Exception 异常
     */
    public String uploadFile(MultipartFile file) throws Exception {
        // 判断文件是否为空
        if (file == null || file.getSize() == 0) {
            log.error("==> 上传文件异常：文件大小为空 ...");
            throw new RuntimeException("文件大小不能为空");
        }

        // 文件的原始名称
        String originalFileName = file.getOriginalFilename();
        // 文件的 Content-Type
        String contentType = file.getContentType();

        // 生成存储对象的名称（将 UUID 字符串中的 - 替换成空字符串）
        String key = UUID.randomUUID().toString().replace("-", "");
        // 获取文件的后缀，如 .jpg
        String suffix = null;
        if (originalFileName != null) {
            suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        // 拼接上文件后缀，即为要存储的文件名
        String objectName = String.format("%s%s", key, suffix);

        log.info("==> 开始上传文件至 Rustfs, ObjectName: {}", objectName);

        // 上传文件至 Rustfs
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .key(objectName)
                .bucket(rustfsProperties.getBucketName())
                .contentType(contentType)
                .contentLength(file.getSize())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        // 返回文件的访问链接
        String url = String.format("%s/%s/%s", rustfsProperties.getEndpoint(), rustfsProperties.getBucketName(), objectName);
        log.info("==> 上传文件至 Rustfs 成功，访问路径: {}", url);
        return url;
    }
}
