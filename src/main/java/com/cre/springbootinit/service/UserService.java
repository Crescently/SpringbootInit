package com.cre.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cre.springbootinit.model.entity.User;
import com.cre.springbootinit.model.request.user.UserRegisterRequest;
import com.cre.springbootinit.model.request.user.UserUpdateInfoRequest;
import com.cre.springbootinit.model.request.user.UserUpdatePwdRequest;
import com.cre.springbootinit.model.response.user.UserInfoResponse;
import com.cre.springbootinit.model.response.user.UserLoginResponse;

import java.util.Map;

public interface UserService extends IService<User> {
    UserInfoResponse getUserInfoByName(String userAccount);

    void register(UserRegisterRequest userRegisterRequest);

    void updateUserInfo(UserUpdateInfoRequest userUpdateInfoRequest);

    void updateAvatar(String avatarUrl);

    void updatePassword(UserUpdatePwdRequest userUpdatePwdRequest);

    UserLoginResponse login(String userAccount, String userPassword);
}
