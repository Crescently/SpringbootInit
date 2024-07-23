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

    private Integer id;

    private String userAccount;

    private String userRole;

    private String username;

    private String userEmail;

    private String userPic;

}
