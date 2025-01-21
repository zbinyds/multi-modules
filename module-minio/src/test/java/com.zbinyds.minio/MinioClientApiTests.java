package com.zbinyds.minio;

import com.zbinyds.minio.prop.MinioProperties;
import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.messages.Bucket;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zbinyds
 * @since 2025-01-13 10:19
 */

@SpringBootTest
public class MinioClientApiTests {
    private static final Logger log = LoggerFactory.getLogger(MinioClientApiTests.class);
    @Resource
    private MinioClient minioClient;
    @Resource
    private MinioProperties minioProperties;

    @Test
    public void test1() throws Exception {
        List<Bucket> buckets = minioClient.listBuckets();
        for (Bucket bucket : buckets) {
            log.info("bucket name: {}", bucket.name());
            log.info("bucket creationDate: {}", bucket.creationDate());
        }

        BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder()
                .bucket(minioProperties.getBucketName())
                .build();
        boolean flag = minioClient.bucketExists(bucketExistsArgs);
        log.info("minioProperties.getBucketName() exist: {}", flag);

//        MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder().bucket("test-bucket2").build();
//        minioClient.makeBucket(makeBucketArgs);

//        minioClient.removeBucket(RemoveBucketArgs.builder().bucket("test-bucket2").build());
    }
}
