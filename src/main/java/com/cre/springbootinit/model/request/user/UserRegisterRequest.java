package com.cre.springbootinit.model.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户注册请求类
 */
@Data
public class UserRegisterRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户账号
     */
    @NotBlank(message = "用户账号不能为空")
    @Pattern(regexp = "^\\S{5,16}", message = "账号需要5到16位，且不能包含空格")
    private String userAccount;

    /**
     * 用户密码
     */
    @NotBlank(message = "用户密码不能为空")
    @Pattern(regexp = "^\\S{5,16}", message = "密码需要5到16位，且不能包含空格")
    private String userPassword;

    /**
     * 校验密码
     */
    @NotBlank(message = "校验密码不能为空")
    @Pattern(regexp = "^\\S{5,16}", message = "校验密码需要5到16位，且不能包含空格")
    private String checkPassword;

    /**
     * 用户名
     */
    @NotEmpty
    @Pattern(regexp = "\\S{1,10}$", message = "用户名长度为1-10个非空字符")
    private String nickName;

    /**
     * 邮箱
     */
    @Email
    @NotEmpty
    private String userEmail;
}
