minio
- Docker安装配置
    - docker pull minio/minio
    - docker run
        ```
        docker run -d
        -p 19000:9000 -p 19001:9001 \
        --restart=always \
        --name minio \
        -e "MINIO_ROOT_USER=sysadmin" \
        -e "MINIO_ROOT_PASSWORD=123456" \
        -v /opt/minio/data:/data \
        -v /opt/minio/config:/root/.minio \
        minio/minio server /data --console-address "127.0.0.1:9001"
      ```
    - minio页面地址：http://127.0.0.1:9001
- 引入依赖
    ```
    implementation 'io.minio:minio:8.2.2'
  ```
- api调用（MinioClient）
    ```
    // 注册bean，后续调用即可
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
    ```
