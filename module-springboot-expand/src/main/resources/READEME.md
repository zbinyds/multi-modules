springboot expand 拓展点

- EnvironmentPostProcessor
    - yml配置文件加密
    ```text
        使用说明
        1、com.zbinyds.springboot.config.AppEnvironmentPostProcessor
        2、resource/META-INF/spring.factories，添加：org.springframework.boot.env.EnvironmentPostProcessor=com.zbinyds.springboot.config.AppEnvironmentPostProcessor
    ```
- ...

kafka
- docker部署
  - docker-compose.yml
    ```yaml
    version: '3.0'
    services:
      zookeeper:
        restart: always
        image: "zookeeper:3.5"
        ports:
          - "22181:2181"
        environment:
          ZOO_MY_ID: 1
          ZOO_SERVERS: server.1=zookeeper:2888:3888;zookeeper:2181
        volumes:
          - /opt/kafka/zookeeper/data:/data
          - /opt/kafka/zookeeper/datalog:/datalog
      kafka:
        restart: always
        image: wurstmeister/kafka
        depends_on:
          - zookeeper
        ports:
          - "29092:9092"
        environment:
          KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
          KAFKA_ADVERTISED_HOST_NAME: 192.168.1.100
          KAFKA_ADVERTISED_PORT: 29092
        volumes:
          - /opt/kafka/kafka/logs:/opt/kafka/logs
          - /etc/localtime:/etc/localtime
          - /opt/kafka/kafka/var/run/docker.sock:/var/run/docker.sock
    ```
    参考配置： https://github.com/wurstmeister/kafka-docker/blob/master/docker-compose.yml
  - docker-compose相关命令。
    - 启动：docker-compose up -d 
    - 停止并删除容器：docker-compose down
    - 停止：docker-compose stop
  - github：https://github.com/wurstmeister/kafka-docker
  - 其他docker部署的方式github：https://github.com/simplesteph/kafka-stack-docker-compose

- springboot接入kafka
  - 依赖
  ```text
    implementation "org.springframework.kafka:spring-kafka:2.8.4"
  ```
  - 注册topic bean。为每个topic设置分区大小以及副本数量（com.zbinyds.springboot.config.KafkaConfig）
  - 提供生产者、消费者服务。（com.zbinyds.springboot.service.KafkaProducer/KafkaConsumer）
  - 提供接口，模拟生产者发送消息。(com.zbinyds.springboot.controller.KafkaController#sendMessage)

- 相关参考文档
  - 配置文件加解密：https://github.com/ulisesbocchio/jasypt-spring-boot
  - kafka使用：https://github.com/Snailclimb/springboot-kafka/tree/master/docs
  - https://github.com/crisxuan/bestJavaer/blob/master/kafka/kafka-basic.md