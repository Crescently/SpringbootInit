package com.cre.springbootinit.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cre.springbootinit.common.ErrorCode;
import com.cre.springbootinit.constant.MessageConstant;
import com.cre.springbootinit.exception.BusinessException;
import com.cre.springbootinit.mapper.UserMapper;
import com.cre.springbootinit.model.entity.User;
import com.cre.springbootinit.model.request.user.UserRegisterRequest;
import com.cre.springbootinit.model.request.user.UserUpdateInfoRequest;
import com.cre.springbootinit.model.request.user.UserUpdatePwdRequest;
import com.cre.springbootinit.model.response.user.UserInfoResponse;
import com.cre.springbootinit.model.response.user.UserLoginResponse;
import com.cre.springbootinit.service.UserService;
import com.cre.springbootinit.utils.JwtUtil;
import com.cre.springbootinit.utils.LoginUserInfoUtil;
import com.cre.springbootinit.utils.Md5Util;
import com.cre.springbootinit.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public UserInfoResponse getUserInfoByName(String userAccount) {
        // 查询数据库，根据用户名获取用户信息
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_account", userAccount);
        User user = userMapper.selectOne(wrapper);

        UserInfoResponse userInfoResponse = new UserInfoResponse();
        BeanUtils.copyProperties(user, userInfoResponse);
        return userInfoResponse;
    }

    @Override
    public void register(UserRegisterRequest userRegisterRequest) {
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String nickName = userRegisterRequest.getNickName();
        String userEmail = userRegisterRequest.getUserEmail();
        // 检查两次密码是否一致
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, MessageConstant.TWO_PWD_NOT_MATCH);
        }

        synchronized (userAccount.intern()) {
            // 查询数据库是否已有账户名
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.select("*").eq("user_account", userAccount);
            User user = userMapper.selectOne(wrapper);
            if (user != null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, MessageConstant.USERNAME_ALREADY_EXIST);
            }
            // 首先对密码进行加密，保证安全性
            String md5Password = Md5Util.getMD5String(userPassword);
            user = User.builder().userAccount(userAccount).userPassword(md5Password).nickName(nickName).userEmail(userEmail).build();
            // 将用户信息插入数据库
            userMapper.insert(user);
        }
    }

    @Override
    public UserLoginResponse login(String userAccount, String userPassword) {
        // 根据用户名查询用户
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_account", userAccount);
        User loginUser = userMapper.selectOne(wrapper);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, MessageConstant.USERNAME_NOT_EXIST);
        }
        // 判断密码正确性
        if (Md5Util.getMD5String(userPassword).equals(loginUser.getUserPassword())) {
            // 登录成功，返回JWT令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", loginUser.getId());
            claims.put("userAccount", loginUser.getUserAccount());
            String token = JwtUtil.genToken(claims);
            // 把token存入redis
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token, token, 1, TimeUnit.HOURS);

            // 封装返回值
            UserLoginResponse userLoginResponse = new UserLoginResponse();
            userLoginResponse.setToken(token);
            BeanUtils.copyProperties(loginUser, userLoginResponse);
            return userLoginResponse;
        }
        throw new BusinessException(ErrorCode.PARAMS_ERROR, MessageConstant.PASSWORD_ERROR);
    }

    @Override
    public void updateUserInfo(UserUpdateInfoRequest userUpdateInfoRequest) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        User user = User.builder().id(id).nickName(userUpdateInfoRequest.getNickName()).userEmail(userUpdateInfoRequest.getUserEmail()).build();
        userMapper.update(user, new QueryWrapper<User>().eq("id", id));
    }

    /*
    更新头像
     */
    @Override
    public void updateAvatar(String avatarUrl) {
        User user = User.builder().userPic(avatarUrl).build();
        userMapper.update(user, new QueryWrapper<User>().eq("id", LoginUserInfoUtil.getUserId()));
    }

    @Override
    public void updatePassword(UserUpdatePwdRequest userUpdatePwdRequest) {
        String oldPassword = userUpdatePwdRequest.getOldPassword();
        String newPassword = userUpdatePwdRequest.getNewPassword();
        String rePassword = userUpdatePwdRequest.getRePassword();
        // 原密码是否正确
        Map<String, Object> map = ThreadLocalUtil.get();
        String userAccount = (String) map.get("userAccount");
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("*").eq("user_account", userAccount);
        User loginUser = userMapper.selectOne(wrapper);
        if (!Md5Util.checkPassword(oldPassword, loginUser.getUserPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, MessageConstant.OLD_PWD_ERROR);
        }
        if (!newPassword.equals(rePassword)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, MessageConstant.TWO_PWD_NOT_MATCH);
        }

        String md5String = Md5Util.getMD5String(newPassword);
        // 2.更新数据库
        User user = User.builder().userPassword(md5String).build();
        userMapper.update(user, new QueryWrapper<User>().eq("id", LoginUserInfoUtil.getUserId()));
    }

}
