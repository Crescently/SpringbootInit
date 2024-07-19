-- 创建数据库
create database my_database;

-- 使用数据库
use my_database;

-- 用户表
create table user
(
    id            int unsigned primary key auto_increment comment 'ID',
    user_account  varchar(20)            not null unique comment '账号',
    user_password varchar(32) comment '密码',
    nickName      varchar(10)  default '' comment '昵称',
    user_role     varchar(10)  default '' comment '用户权限',
    user_email    varchar(128) default '' comment '邮箱',
    user_pic      varchar(128) default '' comment '头像',
    create_time   datetime               not null comment '创建时间',
    update_time   datetime               not null comment '修改时间',
    isDelete      tinyint      default 0 not null comment '是否删除',
    description   varchar(255) default '' comment '个人简介'
) comment '用户表';

