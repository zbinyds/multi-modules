package com.zbinyds.learn.satoken.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xmm.service.XmmOssService;
import com.zbinyds.learn.satoken.mapper.SysUserMapper;
import com.zbinyds.learn.satoken.model.entity.SysUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

/**
 * @Author zbinyds
 * @Create 2024-11-20 13:29
 */

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final SysUserMapper sysUserMapper;
    private final XmmOssService xmmOssService;

    @SaCheckPermission("user:list")
    @GetMapping("/info")
    public String getUserInfo(String username) {
        log.info("username: {}", username);
        SysUser sysUser = sysUserMapper.selectOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, username));
        log.info("sysUser: {}", sysUser);
        return "success";
    }

    @SaCheckPermission("user:check")
    @GetMapping("/check")
    public String check(String username) {
        log.info("username: {}", username);
        return "success";
    }

    @SaCheckPermission("file:upload")
    @PostMapping("/upload")
    public String upload(MultipartFile file) throws IOException {
        return xmmOssService.upload(file.getInputStream(), Objects.requireNonNull(file.getOriginalFilename()));
    }

    @SaCheckPermission("file:download")
    @GetMapping("/download")
    public void download(@RequestParam String filePath) {
        String fileSuffix = filePath.substring(filePath.lastIndexOf("."));
        xmmOssService.download(filePath, "D:/test/1/test" + fileSuffix);
    }

    @SaCheckPermission("file:delete")
    @PostMapping("/delete")
    public boolean delete(@RequestParam String filePath, @RequestParam boolean isQuiet) {
        return xmmOssService.deleteFileBatch(isQuiet, filePath);
    }
}
