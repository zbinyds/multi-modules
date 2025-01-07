package com.zbinyds.springboot.config;

import com.zbinyds.springboot.util.AESUtil;
import org.apache.commons.logging.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.boot.logging.DeferredLogFactory;
import org.springframework.boot.origin.OriginTrackedValue;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zbinyds
 * @since 2024-12-27 14:11
 */
public class AppEnvironmentPostProcessor implements EnvironmentPostProcessor {
    private static final String DEFAULT_ENCRYPTOR_PREFIX = "ENC(";
    private static final String DEFAULT_ENCRYPTOR_SUFFIX = ")";
    private final Log log;

    public AppEnvironmentPostProcessor(DeferredLogFactory deferredLogFactory) {
        this.log = deferredLogFactory.getLog(getClass());
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        log.info("[AppEnvironmentPostProcessor] postProcessEnvironment.");
        Map<String, OriginTrackedValue> replaceSource = new HashMap<>();
        OriginTrackedMapPropertySource encryptorPropertySource = new OriginTrackedMapPropertySource("encryptor", replaceSource);

        for (PropertySource<?> propertySource : environment.getPropertySources()) {
            // 只需要找到application配置即可
            if (propertySource.getName().contains("application")) {
                Map<String, OriginTrackedValue> source = safeGetSource(propertySource);

                source.forEach((k, v) -> {
                    String value = v.getValue().toString().trim();
                    String prefix = environment.getProperty("encryptor.prefix", DEFAULT_ENCRYPTOR_PREFIX);
                    String suffix = environment.getProperty("encryptor.suffix", DEFAULT_ENCRYPTOR_SUFFIX);

                    if (value.startsWith(prefix) && value.endsWith(suffix)) {
                        String key = environment.getProperty("encryptor.key");
                        Assert.notNull(key, "encryptor.key 不能为空");

                        // 解密
                        try {
                            value = unwrapEncryptedValue(value, prefix, suffix);
                            replaceSource.put(k, OriginTrackedValue.of(AESUtil.decrypt(key, value), v.getOrigin()));
                        } catch (Exception e) {
                            log.error("config key: " + k + " decrypt value error: " + e.getMessage());
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        }
        // 自定义PropertySource 优先级设置为最高 用解密后的配置项覆盖配置文件中同key的配置项 从而实现解密
        environment.getPropertySources().addFirst(encryptorPropertySource);
    }

    @SuppressWarnings("unchecked")
    private Map<String, OriginTrackedValue> safeGetSource(PropertySource<?> propertySource) {
        if (null != propertySource) {
            Object source = propertySource.getSource();
            if (source instanceof Map) {
                return (Map<String, OriginTrackedValue>) source;
            }
        }
        return Collections.emptyMap();
    }

    private String unwrapEncryptedValue(String value, String prefix, String suffix) {
        return value.substring(prefix.length(), (value.length() - suffix.length()));
    }
}
