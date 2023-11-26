package org.phenomenal.toolkit.entities;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "oss")
public class OssProperty {
    String endpoint;
    String accessKey;
    String accessSecret;
    String bucketName;
}
