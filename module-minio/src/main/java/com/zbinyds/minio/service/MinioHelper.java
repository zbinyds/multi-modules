package com.zbinyds.minio.service;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author zbinyds
 * @since 2025-01-11 16:05
 */

@SuppressWarnings({"unused"})
public interface MinioHelper {
    String uploadFile(InputStream inputStream, String fileName, String directory);

    boolean createDirectory(String directory);

    boolean deleteFile(String object);

    boolean deleteFile(List<String> objects);

    String getFileUrl(String object);

    String getFileUrl(String object, int duration, TimeUnit expireUnit);
}
