package com.zbinyds.learn.satoken.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbinyds.learn.satoken.mapper.*;
import com.zbinyds.learn.satoken.model.entity.*;
import com.zbinyds.learn.satoken.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author zbinyds
 * @since 2024-11-25 08:38
 */

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements UserService {
    private final SysUserMapper userMapper;
    private final SysUserRoleMapper userRoleMapper;
    private final SysRoleMapper roleMapper;
    private final SysResourceMapper resourceMapper;
    private final SysRoleResourceMapper roleResourceMapper;

    @Override
    public List<SysRole> getUserRole(String username) {
        SysUser sysUser = Optional.ofNullable(userMapper.selectOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, username)))
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        List<SysUserRole> userRoleList = userRoleMapper.selectList(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, sysUser.getId()));
        List<Long> roleIds = userRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        if (roleIds.isEmpty()) {
            return Collections.emptyList();
        }
        return roleMapper.selectBatchIds(roleIds);
    }

    @Override
    public List<SysResource> getUserResource(String username) {
        List<SysRole> roleList = getUserRole(username);
        if (roleList.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<SysRoleResource> wrapper = Wrappers.<SysRoleResource>lambdaQuery().in(SysRoleResource::getRoleId,
                roleList.stream().map(SysRole::getId).collect(Collectors.toList()));
        List<SysRoleResource> roleResourceList = roleResourceMapper.selectList(wrapper);
        List<Long> resourceIds = roleResourceList.stream().map(SysRoleResource::getResourceId).collect(Collectors.toList());
        return roleResourceList.isEmpty() ? Collections.emptyList() : resourceMapper.selectBatchIds(resourceIds);
    }
}
