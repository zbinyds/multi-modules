package com.zbinyds.learn.satoken.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SysUser implements Serializable {
    private static final long serialVersionUID = -8714138391950287958L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;

    /**
     * 是否为内部人员(0-不是 1-是)，默认为0
     */
    @Schema(description = "是否为内部人员(0-不是 1-是)，默认为0", defaultValue = "false")
    private Boolean internal;

    /**
     * 微信平台唯一标识id
     */
    @Schema(description = "微信平台唯一标识id")
    private String openid;

    /**
     * 用户姓名
     */
    @Schema(description = "用户姓名")
    private String nickname;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String phone;

    /**
     * 身份证
     */
    @Schema(description = "身份证")
    private String idCard;

    /**
     * 性别(0-男 1-女 2-其他)，默认为0
     */
    @Schema(description = "性别(0-男 1-女 2-其他)，默认为0", defaultValue = "0")
    private Integer sex;

    /**
     * 电子邮箱
     */
    @Schema(description = "电子邮箱")
    private String email;

    /**
     * 头像url
     */
    @Schema(description = "头像url")
    private String headSculpture;

    /**
     * 逻辑删除，默认为0
     */
    @TableLogic
    @Schema(description = "逻辑删除，默认为0", defaultValue = "false")
    private Boolean del;

    /**
     * 创建时间，默认为当前时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间，默认为当前时间")
    private LocalDateTime createdTime;

    /**
     * 修改时间，默认为当前时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "修改时间，默认为当前时间")
    private LocalDateTime updatedTime;
}
