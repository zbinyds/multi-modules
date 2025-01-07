package com.zbinyds.springboot.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class AESUtil {

    // 生成随机AES密钥（以Base64编码返回）
    public static String generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256, new SecureRandom()); // 使用256位密钥（确保JCE支持）
        SecretKey secretKey = keyGen.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    // 加密方法：输入密钥和原始密码，返回加密后的密码
    public static String encrypt(String key, String plainText) throws Exception {
        // 还原密钥
        SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.getDecoder().decode(key), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // 解密方法：输入密钥和加密后的密码，返回解密后的原密码
    public static String decrypt(String key, String encryptedText) throws Exception {
        // 还原密钥
        SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.getDecoder().decode(key), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) {
        try {
            // 测试工具类
            String secretKey = generateKey(); // 生成密钥
            System.out.println("Secret Key: " + secretKey);

            String originalPassword = "192.168.1.100:29092";
            System.out.println("Original Password: " + originalPassword);

            // 加密
            String encryptedPassword = encrypt(secretKey, originalPassword);
            System.out.println("Encrypted Password: " + encryptedPassword);

            // 解密
            String decryptedPassword = decrypt(secretKey, encryptedPassword);
            System.out.println("Decrypted Password: " + decryptedPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}