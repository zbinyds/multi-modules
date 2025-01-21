package com.zbinyds.minio.prop;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zbinyds
 * @since 2025-01-11 14:48
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {
    private String endpoint;

    private String accessKey;

    private String secretKey;

    private String bucketName;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}
