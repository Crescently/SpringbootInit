package com.cre.springbootinit.model.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户登录请求体
 */
@Data
public class UserLoginRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "用户账号不能为空")
    @Pattern(regexp = "^\\S{5,16}", message = "账号需要5到16位，且不能包含空格")
    String userAccount;

    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^\\S{5,16}", message = "密码需要5到16位，且不能包含空格")
    String userPassword;
}
