package com.cre.springbootinit.controller;

import com.cre.springbootinit.constant.MessageConstant;
import com.cre.springbootinit.pojo.common.BaseResponse;
import com.cre.springbootinit.pojo.entity.User;
import com.cre.springbootinit.service.UserService;
import com.cre.springbootinit.utils.JwtUtil;
import com.cre.springbootinit.utils.Md5Util;
import com.cre.springbootinit.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Validated // 参数校验工具
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 用户注册
     *
     * @param username 用户名
     * @param password 密码
     */
    @PostMapping("/register")
    public BaseResponse userRegister(@Pattern(regexp = "^\\S{5,16}") String username, @Pattern(regexp = "^\\S{5,16}") String password) {
        log.info("用户注册，用户名：{}，密码：{}", username, password);
        // 1.查询用户名是否已经被注册
        User user = userService.getUserInfoByName(username);
        if (user == null) {
            // 2.如果没有，进行注册
            userService.register(username, password);
            return BaseResponse.success();
        } else {
            return BaseResponse.error(MessageConstant.USERNAME_EXIST);
        }
    }

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     */
    @PostMapping("/login")
    public BaseResponse userLogin(@Pattern(regexp = "^\\S{5,16}") String username, @Pattern(regexp = "^\\S{5,16}") String password) {
        log.info("用户登录，用户名：{}，密码：{}", username, password);
        // 1.根据用户名查询用户
        User loginUser = userService.getUserInfoByName(username);
        if (loginUser == null) {
            return BaseResponse.error(MessageConstant.USERNAME_ERROR);
        }
        // 判断密码正确性
        if (Md5Util.getMD5String(password).equals(loginUser.getUserPassword())) {
            // 登录成功，返回JWT令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", loginUser.getId());
            claims.put("username", loginUser.getUserAccount());
            String token = JwtUtil.genToken(claims);
            // 把token存入redis
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token, token, 1, TimeUnit.HOURS);
            return BaseResponse.success(token);
        }
        return BaseResponse.error(MessageConstant.PASSWORD_ERROR);
    }

    /**
     * 获取用户信息
     *
     * @return User对象
     */
    @GetMapping("/userInfo")
    public BaseResponse<User> getUserInfo() {

        // 使用ThreadLocal获取用户名
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        log.info("当前登录的用户名：{}", username);
        // 2.查询数据库
        User userInfo = userService.getUserInfoByName(username);

        return BaseResponse.success(userInfo);
    }

    /**
     * 更新用户信息
     *
     * @param user 用户数据
     */
    @PutMapping("/update")
    public BaseResponse updateUserInfo(@RequestBody @Validated User user) {
        log.info("用户信息更新");
        userService.updateUserInfo(user);
        return BaseResponse.success();
    }

    /**
     * 更新头像
     *
     * @param avatarUrl 头像URL地址
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
     * @param params 更新密码
     * @param token  请求头中的用户token
     */
    @PatchMapping("/updatePwd")
    public BaseResponse updatePassword(@RequestBody Map<String, String> params, @RequestHeader("Authorization") String token) {
        log.info("更新密码");
        // 1.校验参数
        String oldPassword = params.get("old_pwd");
        String newPassword = params.get("new_pwd");
        String rePassword = params.get("re_pwd");
        if (!StringUtils.hasLength(oldPassword) || !StringUtils.hasLength(newPassword) || !StringUtils.hasLength(rePassword)) {
            return BaseResponse.error(MessageConstant.PARAM_ERROR);
        }
        // 原密码是否正确
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User loginUser = userService.getUserInfoByName(username);
        if (!Md5Util.checkPassword(oldPassword, loginUser.getUserPassword())) {
            return BaseResponse.error(MessageConstant.OLD_PWD_ERROR);
        }
        if (!newPassword.equals(rePassword)) {
            return BaseResponse.error(MessageConstant.TWO_PWD_NOT_MATCH);
        }
        userService.updatePassword(newPassword);
        // 删除redis中的token
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(token);

        return BaseResponse.success();
    }
}
