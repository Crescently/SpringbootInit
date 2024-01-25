package com.cre.springbootinit.pojo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    private Integer id;//主键ID

    private String userAccount;//用户名

    @JsonIgnore // 转成JSON时忽略该字段
    private String userPassword;//密码

    @NotEmpty
    @Pattern(regexp = "\\S{1,10}$")
    private String nickName;//昵称

    @Email @NotEmpty //非空字符串
    private String userEmail;//邮箱

    private String userPic;//用户头像地址

    private LocalDateTime createTime;//创建时间

    private LocalDateTime updateTime;//更新时间
}
