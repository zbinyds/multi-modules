create table local_message
(
    id              bigint auto_increment
        primary key,
    create_time     datetime default CURRENT_TIMESTAMP not null comment 'create time',
    update_time     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'update time',
    del             tinyint  default 0                 not null comment 'deleted at',
    message_type    int                                not null comment '消息类别',
    message_id      bigint                             not null comment '消息全局id',
    req_snapshot    json                               not null comment '请求快照参数json',
    status          varchar(16)                        not null comment '状态 INIT, FAIL, SUCCESS, RETRY',
    next_retry_time bigint                             not null comment '下一次重试的时间',
    retry_times     int      default 0                 not null comment '已经重试的次数',
    max_retry_times int                                not null comment '最大重试次数',
    fail_reason     text                               null comment '执行失败的信息'
)
    comment '本地消息表';