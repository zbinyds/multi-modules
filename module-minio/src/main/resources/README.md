minio
- Docker安装配置
    - docker pull minio/minio
    - docker run
        ```
        docker run -p 9000:9000 -p 9001:9001 \
        --restart=always \
        -e "MINIO_ROOT_USER=sysadmin" \
        -e "MINIO_ROOT_PASSWORD=123qwe!@#" \
        -v D:\zbinyds\minio\data:/data \
        -v D:\zbinyds\minio\config:/root/.minio \
        minio/minio server /data --console-address "127.0.0.1:9001" \
        --name minio -d
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
