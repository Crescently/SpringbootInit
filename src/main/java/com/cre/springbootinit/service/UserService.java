package com.cre.springbootinit.service;

import com.cre.springbootinit.pojo.entity.User;

public interface UserService {
    User getUserInfoByName(String username);

    void register(String username, String password);

    void updateUserInfo(User user);

    void updateAvatar(String avatarUrl);

    void updatePassword(String newPassword);
}
