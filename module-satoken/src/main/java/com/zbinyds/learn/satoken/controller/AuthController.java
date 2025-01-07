package com.zbinyds.learn.satoken.controller;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import com.zbinyds.learn.satoken.model.entity.SysUser;
import com.zbinyds.learn.satoken.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @Author zbinyds
 * @Create 2024-11-19 16:59
 */

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping("/login")
    public String login(String username, String password) {
        Optional<SysUser> sysUser = userService.lambdaQuery().eq(SysUser::getUsername, username).oneOpt();

        if (sysUser.isPresent() && BCrypt.checkpw(password, sysUser.get().getPassword())) {
            StpUtil.login(username);
            return "登录成功";
        }
        return "登录失败";
    }

    @GetMapping("/loginOut")
    public String loginOut() {
        StpUtil.logout();
        return "success";
    }
}
