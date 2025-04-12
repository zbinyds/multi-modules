create table local_message
(
    id              bigint auto_increment
        primary key,
    create_time     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间',
    del             tinyint  default 0                 not null comment '逻辑删除',
    message_type    int                                not null comment '消息类别 由业务指定',
    message_id      bigint                             not null comment '消息全局id 由业务指定',
    req_snapshot    json                               not null comment '请求快照参数json',
    status          varchar(16)                        not null comment '消息状态 INIT, FAIL, RETRY',
    next_retry_time datetime                           not null comment '下一次重试的时间',
    retry_times     int      default 0                 not null comment '已经重试的次数',
    max_retry_times int                                not null comment '最大重试次数',
    fail_reason     text                               null comment '执行失败的信息'
)
    comment '本地消息表';