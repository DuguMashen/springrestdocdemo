create table ts_user
(
    id          int auto_increment
        primary key,
    name        varchar(32) default ''                  not null comment '用户名',
    password    varchar(128)                            not null comment '密码',
    status      varchar(8)  default 'init'              null comment '用户状态',
    create_time datetime    default current_timestamp() not null comment '创建时间',
    update_time datetime    default current_timestamp() not null on update current_timestamp() comment '更新时间'
)
    comment '用户';