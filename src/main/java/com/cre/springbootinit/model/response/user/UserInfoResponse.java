package com.cre.springbootinit.model.response.user;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户信息相应体
 */
@Data
public class UserInfoResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String userEmail;

    /**
     * 用户角色
     */
    private String userRole;

    /**
     * 用户头像地址
     */
    private String userPic;

}
