package com.cre.springbootinit.model.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户信息更新请求体
 */
@Data
public class UserUpdateInfoRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotEmpty
    @Pattern(regexp = "\\S{1,10}$", message = "用户名长度为1-10个非空字符")
    private String nickName;//昵称

    @Email
    @NotEmpty //非空字符串
    private String userEmail;//邮箱


}
