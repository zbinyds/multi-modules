package com.zbinyds.minio.controller;

import com.zbinyds.minio.service.MinioHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author zbinyds
 * @since 2025-01-11 14:51
 */

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/minio")
@RequiredArgsConstructor
public class MinioController {
    private final MinioHelper minioHelper;

    @PostMapping("/upload")
    public String upload(MultipartFile file) throws IOException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String directory = LocalDate.now().format(formatter);
        String object = minioHelper.uploadFile(file.getInputStream(), file.getOriginalFilename(), directory);

        stopWatch.stop();

        log.info("upload cost: {} ms", stopWatch.getTotalTimeMillis());
        String urlExpire = minioHelper.getFileUrl(object, 1, TimeUnit.MINUTES);
        log.info("urlExpire: {}", urlExpire);
        return minioHelper.getFileUrl(object);
    }

    @PostMapping("/createDirectory")
    public boolean createDirectory(String directory) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        boolean flag = minioHelper.createDirectory(directory);

        stopWatch.stop();

        log.info("createDirectory cost: {} ms", stopWatch.getTotalTimeMillis());
        return flag;
    }

    @DeleteMapping("/delete")
    public boolean delete(String filename) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        boolean flag = minioHelper.deleteFile(List.of(filename));

        stopWatch.stop();

        log.info("delete cost: {} ms", stopWatch.getTotalTimeMillis());
        return flag;
    }
}
