package com.cre.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cre.springbootinit.model.entity.User;
import com.cre.springbootinit.model.request.admin.UpdateUserRoleRequest;
import com.cre.springbootinit.model.request.user.UserRegisterRequest;
import com.cre.springbootinit.model.request.user.UserUpdateInfoRequest;
import com.cre.springbootinit.model.request.user.UserUpdatePwdRequest;
import com.cre.springbootinit.model.response.user.UserInfoResponse;
import com.cre.springbootinit.model.response.user.UserLoginResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService extends IService<User> {
    /**
     * 根据用户账号获取用户信息
     * @param userAccount 用户账号
     * @return UserInfoResponse
     */
    UserInfoResponse getUserInfoByAccount(String userAccount);

    /**
     * 用户注册
     * @param userRegisterRequest userRegisterRequest
     */
    void register(UserRegisterRequest userRegisterRequest);

    /**
     * 更新用户信息
     * @param userUpdateInfoRequest userUpdateInfoRequest
     */
    void updateUserInfo(UserUpdateInfoRequest userUpdateInfoRequest);

    /**
     * 更新用户头像
     * @param avatarUrl 头像URL
     */
    void updateAvatar(String avatarUrl);

    /**
     * 更新用户密码
     * @param userUpdatePwdRequest userUpdatePwdRequest
     */
    void updatePassword(UserUpdatePwdRequest userUpdatePwdRequest);

    /**
     * 用户登录
     * @param userAccount 用户账号
     * @param userPassword 密码
     * @param request request
     * @return UserLoginResponse
     */
    UserLoginResponse login(String userAccount, String userPassword,HttpServletRequest request);

    /**
     * 用户登出
     * @param request request
     * @return 成功与否
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 更新用户角色
     * @param updateUserRoleRequest updateUserRoleRequest
     */
    void updateUserRole(UpdateUserRoleRequest updateUserRoleRequest);

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);
}
