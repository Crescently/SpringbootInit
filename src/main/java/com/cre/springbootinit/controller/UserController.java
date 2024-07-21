package com.cre.springbootinit.controller;

import com.cre.springbootinit.common.BaseResponse;
import com.cre.springbootinit.common.ErrorCode;
import com.cre.springbootinit.exception.BusinessException;
import com.cre.springbootinit.model.request.user.UserLoginRequest;
import com.cre.springbootinit.model.request.user.UserRegisterRequest;
import com.cre.springbootinit.model.request.user.UserUpdateInfoRequest;
import com.cre.springbootinit.model.request.user.UserUpdatePwdRequest;
import com.cre.springbootinit.model.response.user.UserInfoResponse;
import com.cre.springbootinit.model.response.user.UserLoginResponse;
import com.cre.springbootinit.service.UserService;
import com.cre.springbootinit.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 用户注册
     *
     * @param userRegisterRequest 用户注册请求体
     * @return BaseResponse
     */
    @PostMapping("/register")
    public BaseResponse userRegister(@Validated @RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        log.info("用户注册，用户名：{}，密码：{}", userRegisterRequest.getUserAccount(), userRegisterRequest.getUserPassword());
        userService.register(userRegisterRequest);
        return BaseResponse.success();
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 用户登录请求体
     * @return BaseResponse
     */
    @PostMapping("/login")
    public BaseResponse<UserLoginResponse> userLogin(@Validated @RequestBody UserLoginRequest userLoginRequest) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        log.info("用户登录，用户账号：{}，密码：{}", userAccount, userPassword);
        UserLoginResponse userLoginResponse = userService.login(userAccount, userPassword);
        return BaseResponse.success(userLoginResponse);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/userInfo")
    public BaseResponse<UserInfoResponse> getUserInfo() {
        // 使用ThreadLocal获取用户名
        Map<String, Object> map = ThreadLocalUtil.get();
        String userAccount = (String) map.get("userAccount");
        log.info("当前登录的用户账号：{}", userAccount);
        // 2.查询数据库
        UserInfoResponse userInfoResponse = userService.getUserInfoByName(userAccount);

        return BaseResponse.success(userInfoResponse);
    }

    /**
     * 更新用户信息
     *
     * @param userUpdateInfoRequest 用户信息更新请求体
     * @return BaseResponse
     */
    @PutMapping("/update")
    public BaseResponse updateUserInfo(@RequestBody @Validated UserUpdateInfoRequest userUpdateInfoRequest) {
        log.info("用户信息更新");
        userService.updateUserInfo(userUpdateInfoRequest);
        return BaseResponse.success();
    }

    /**
     * 更新头像
     *
     * @param avatarUrl 头像url地址
     */
    @PatchMapping("/updateAvatar")
    public BaseResponse updateAvatar(@RequestParam @URL String avatarUrl) {
        log.info("更新头像");
        userService.updateAvatar(avatarUrl);
        return BaseResponse.success();
    }

    /**
     * 更新密码
     *
     * @param userUpdatePwdRequest 用户密码更新请求体
     * @param token                token
     * @return BaseResponse
     */
    @PatchMapping("/updatePwd")
    public BaseResponse updatePassword(@RequestBody UserUpdatePwdRequest userUpdatePwdRequest, @RequestHeader("Authorization") String token) {
        log.info("更新密码");
        userService.updatePassword(userUpdatePwdRequest);
        // 删除redis中的token
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(token);

        return BaseResponse.success();
    }
}
