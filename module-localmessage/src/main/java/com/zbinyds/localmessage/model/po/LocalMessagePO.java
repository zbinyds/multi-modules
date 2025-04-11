package com.zbinyds.localmessage.model.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author zbinyds@126.com
 * @since 2025-04-04 19:14
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("`local_message`")
public class LocalMessagePO {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    private Boolean del;

    private Integer messageType;

    private Long messageId;

    private String reqSnapshot;

    private String status;

    private Date nextRetryTime;

    private Integer retryTimes;

    private Integer maxRetryTimes;

    private String failReason;
}
