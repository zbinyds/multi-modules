package com.zbinyds.learn.satoken;

import cn.dev33.satoken.secure.BCrypt;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author zbinyds
 * @since 2024-11-22 16:42
 */

@SpringBootTest
public class CommonTests {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(CommonTests.class);

    @Resource
    private StringEncryptor stringEncryptor;

    @Test
    public void test() {
        String hashpw = BCrypt.hashpw("123456", BCrypt.gensalt());
        log.info("hashpw: {}", hashpw);

        String decrypt1 = stringEncryptor.decrypt("GdRuCZqlXx813Y0flfNrRtWrfG1RWakIS3W1GoHT2616dcd02aSCMczQgjqthZETwssxUgrs5rDz+JGM+WcDaw==");
        log.info("decrypt1: {}", decrypt1);
        String decrypt2 = stringEncryptor.decrypt("L1Xmjn/yWOLi+7AY/qbbQmWk6mU3wwr+r6vuPq8GAY6SIddwi2auFKu4cS/aXMp/zmPIpBgzm6eWAelnQi76rQ==");
        log.info("decrypt2: {}", decrypt2);
    }
}
