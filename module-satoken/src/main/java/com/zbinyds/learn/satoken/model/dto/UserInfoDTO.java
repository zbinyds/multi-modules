package com.zbinyds.learn.satoken.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zbinyds
 * @since 2024-11-25 08:44
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO implements Serializable {
    private static final long serialVersionUID = 4170871560515056035L;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 是否为内部人员(0-不是 1-是)，默认为0
     */
    @Schema(description = "是否为内部人员(0-不是 1-是)，默认为0", defaultValue = "false")
    private Boolean internal;

    /**
     * 用户类型(1-普通用户 2-会员 3-超级会员)，默认为1
     */
    @Schema(description = "用户类型(1-普通用户 2-会员 3-超级会员)，默认为1", defaultValue = "1")
    private Integer userType;

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
}
