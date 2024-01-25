-- 创建数据库
create database my_database;

-- 使用数据库
use my_database;

-- 用户表
create table user
(
    id           int unsigned primary key auto_increment comment 'ID',
    userAccount  varchar(20)            not null unique comment '账号',
    userPassword varchar(32) comment '密码',
    nickName     varchar(10)  default '' comment '昵称',
    userEmail    varchar(128) default '' comment '邮箱',
    userPic      varchar(128) default '' comment '头像',
    createTime   datetime               not null comment '创建时间',
    updateTime   datetime               not null comment '修改时间',
    isDelete     tinyint      default 0 not null comment '是否删除'
) comment '用户表';

