package com.zbinyds.learn.satoken.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SysResource implements Serializable {
    private static final long serialVersionUID = -8714138391950287958L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 类型(1-菜单 2-按钮)
     */
    @Schema(description = "类型(1-菜单 2-按钮)")
    private Integer type;

    /**
     * 父级id(没有默认为0)
     */
    @Schema(description = "父级id(没有默认为0)")
    private Long parentId;

    /**
     * 父级id集合
     */
    @Schema(description = "父级id集合")
    private String parentIds;

    /**
     * 标题
     */
    @Schema(description = "标题")
    private String title;

    /**
     * 权限标志符
     */
    @Schema(description = "权限标志符")
    private String permission;

    /**
     * 路径(菜单才有)
     */
    @Schema(description = "路径(菜单才有)")
    private String path;

    /**
     * 组件名称(菜单才有)
     */
    @Schema(description = "组件名称(菜单才有)")
    private String componentName;

    /**
     * 组件路径(菜单才有)
     */
    @Schema(description = "组件路径(菜单才有)")
    private String componentPath;

    /**
     * 图标
     */
    @Schema(description = "图标")
    private String icon;

    /**
     * 排序，默认为'1'
     */
    @Schema(description = "排序，默认为'1'", defaultValue = "1")
    private String sort;

    /**
     * 菜单是否缓存(0-不缓存 1-缓存)，默认为0
     */
    @Schema(description = "菜单是否缓存(0-不缓存 1-缓存)，默认为0", defaultValue = "false")
    private Boolean menuCacheFlag;

    /**
     * 菜单是否隐藏背景板(0-不隐藏 1-隐藏)，默认为0
     */
    @Schema(description = "菜单是否隐藏背景板(0-不隐藏 1-隐藏)，默认为0", defaultValue = "false")
    private Boolean menuHiddenFlag;

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
     * 更新时间，默认为当前时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间，默认为当前时间")
    private LocalDateTime updatedTime;
}
