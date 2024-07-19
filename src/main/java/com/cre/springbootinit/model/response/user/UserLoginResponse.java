package com.cre.springbootinit.model.response.user;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户登录响应
 */
@Data
public class UserLoginResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String token;

    private Integer id;//主键ID

    private String userAccount;//用户名

    private String userRole;

    private String nickName;//昵称

    private String userEmail;//邮箱

    private String userPic;//用户头像地址

    private LocalDateTime createTime;//创建时间

    private LocalDateTime updateTime;//更新时间
}
