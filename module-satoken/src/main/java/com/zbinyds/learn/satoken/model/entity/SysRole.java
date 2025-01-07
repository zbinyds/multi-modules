package com.zbinyds.learn.satoken.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SysRole implements Serializable {
    private static final long serialVersionUID = -8714138391950287958L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 角色名称
     */
    @Schema(description = "角色名称")
    private String name;

    /**
     * 角色编码,固定前缀为ROLE_
     */
    @Schema(description = "角色编码,固定前缀为ROLE_")
    private String code;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

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
