package com.zbinyds.learn.satoken.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbinyds.learn.satoken.model.entity.SysResource;
import com.zbinyds.learn.satoken.model.entity.SysRole;
import com.zbinyds.learn.satoken.model.entity.SysUser;

import java.util.List;

/**
 * @author zbinyds
 * @since 2024-11-25 08:37
 */

public interface UserService extends IService<SysUser> {
    List<SysRole> getUserRole(String username);

    List<SysResource> getUserResource(String username);
}
