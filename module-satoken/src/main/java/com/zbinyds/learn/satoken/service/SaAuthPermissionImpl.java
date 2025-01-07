package com.zbinyds.learn.satoken.service;

import cn.dev33.satoken.stp.StpInterface;
import com.zbinyds.learn.satoken.model.entity.SysResource;
import com.zbinyds.learn.satoken.model.entity.SysRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author zbinyds
 * @Create 2024-11-21 15:54
 */

@Service
@RequiredArgsConstructor
public class SaAuthPermissionImpl implements StpInterface {
    private final UserService userService;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        if (loginId instanceof String) {
            String username = (String) loginId;
            return userService.getUserResource(username).stream().map(SysResource::getPermission).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        if (loginId instanceof String) {
            String username = (String) loginId;
            return userService.getUserRole(username).stream().map(SysRole::getCode).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
