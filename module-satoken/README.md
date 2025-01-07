knife4j
- 选择open-api3版本依赖，区别在于openapi3依赖的是springdoc，此开源框架维护更新频繁，openapi2依赖的springfox（已经很久没维护了）
> implementation com.github.xiaoymin:knife4j-openapi3-spring-boot-starter:4.4.0

- 配置相关：使用yml配置方式
具体配置项：
  - springdoc：https://springdoc.org/#properties
  - knife4j：https://doc.xiaominfo.com/docs/features/enhance
  - openapi：https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md
```yaml
# 使用knife4j增强（不需要可以不开启）
knife4j:
  enable: true
# springdoc配置
springdoc:
  swagger-ui:
    path: /swagger-ui.html
  group-configs:
    - group: 'default'
      paths-to-match: '/**'
      packages-to-scan: com.zbinyds.learn.satoken
```
```java
/**
 * openapi 相关配置
 */
@Configuration
public class Knife4jConfiguration {
    @Bean
    public OpenAPI openApi() {
        return new OpenAPI().info(
                new Info()
                        .title("接口文档")
                        .description("这是一个接口文档")
                        .version("1.0.0")
                        .contact(new Contact().name("zbinyds").url("www.xxx.com").email("zbinyds@126.com"))
        );
    }
}
```
- 使用方式：
  - knife4j封装的 http://host:port/doc.html 访问即可；
  - springdoc原生的 http://host:port/swagger-ui.html 访问
- 需要放行的静态资源：["/doc.html", "/webjars/**", "/swagger-resources/**", "/v3/**"]

satoken
- 根据需求查阅官方文档即可。地址：https://sa-token.cc/doc.html#/
- 常用插件：
  - sa-token-spring-boot-starter。核心starter依赖
  - aop插件。实现权限校验，与拦截器方式互斥，二选一即可。拦截器方式只能实现controller层权限校验，aop可以具体到方法层。
  - jwt插件。satoken默认以uuid生成随机的token，集成jwt模块以jwt生成token。
  - redis插件(SaTokenDao)。satoken默认使用内存缓存方式实现，接入redis脱离单机模式。插件具体可选jackson，兼容性稍差但可读性强。注：如果需要业务层缓存与权限缓存隔离可以使用redis-alone插件，为satoken单独配置redis库