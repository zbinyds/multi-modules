package com.zbinyds.minio.service.impl;

import com.zbinyds.minio.prop.MinioProperties;
import com.zbinyds.minio.service.MinioHelper;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author zbinyds
 * @since 2025-01-11 16:06
 */

@Service
@RequiredArgsConstructor
public class MinioHelperImpl implements MinioHelper {
    private static final Logger log = LoggerFactory.getLogger(MinioHelperImpl.class);
    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    @Override
    @SneakyThrows
    public String uploadFile(InputStream inputStream, String fileName, String directory) {
        String object = directory + "/" + fileName;
        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket(minioProperties.getBucketName())
                .object(object)
                .stream(inputStream, -1, 10485760)
                .build();
        minioClient.putObject(putObjectArgs);
        log.info("file [{}] upload success, object: [{}]", fileName, object);

        return object;
    }

    @Override
    @SneakyThrows
    public boolean createDirectory(String directory) {
        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket(minioProperties.getBucketName())
                .object(directory + "/")
                .stream(new ByteArrayInputStream(new byte[0]), 0, -1)
                .build();
        return Optional.ofNullable(minioClient.putObject(putObjectArgs))
                .map(response -> {
                    String object = response.object();
                    log.info("directory create success: {}", object);
                    return true;
                }).orElse(false);
    }

    @Override
    @SneakyThrows
    public boolean deleteFile(String object) {
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                .bucket(minioProperties.getBucketName())
                .object(object)
                .bypassGovernanceMode(true)
                .build();
        minioClient.removeObject(removeObjectArgs);
        log.info("delete file or directory success, object: [{}]", object);
        return true;
    }

    @Override
    @SneakyThrows
    public boolean deleteFile(List<String> objects) {
        RemoveObjectsArgs removeObjectsArgs = RemoveObjectsArgs.builder()
                .bucket(minioProperties.getBucketName())
                .objects(objects.stream().map(DeleteObject::new).collect(Collectors.toList()))
                .bypassGovernanceMode(true)
                .build();
        Iterable<Result<DeleteError>> results = minioClient.removeObjects(removeObjectsArgs);
        for (Result<DeleteError> next : results) {
            DeleteError deleteError = next.get();
            log.info("deleteError: {}", deleteError);
        }

        log.info("delete file or directory success, object: {}", objects);
        return true;
    }

    @Override
    @SneakyThrows
    public String getFileUrl(String object) {
        GetPresignedObjectUrlArgs urlArgs = GetPresignedObjectUrlArgs.builder()
                .bucket(minioProperties.getBucketName())
                .object(object)
                .method(Method.GET)
                .build();
        return minioClient.getPresignedObjectUrl(urlArgs);
    }

    @Override
    @SneakyThrows
    public String getFileUrl(String object, int duration, TimeUnit expireUnit) {
        GetPresignedObjectUrlArgs urlArgs = GetPresignedObjectUrlArgs.builder()
                .bucket(minioProperties.getBucketName())
                .expiry(duration, expireUnit)
                .object(object)
                .method(Method.GET)
                .build();
        return minioClient.getPresignedObjectUrl(urlArgs);
    }
}
