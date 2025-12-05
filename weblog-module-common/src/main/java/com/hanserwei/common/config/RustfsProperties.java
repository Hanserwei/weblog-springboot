package com.hanserwei.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "rustfs")
public class RustfsProperties {
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
}
