package com.cre.springbootinit.mapper;

import com.cre.springbootinit.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    //    @Select("select *from user where userAccount = #{username}")
    User getUserInfoByName(String username);

    //    @Insert("insert into user(userAccount,userPassword,createTime,updateTime)" +
//            " values(#{userAccount},#{userPassword},now(),now())")
    void addUserInfo(String username, String password);

    //    @Update("update user set nickName=#{nickName},userEmail=#{userEmail},updateTime=#{updateTime} where id=#{id}")
    void updateUserInfo(User user);

    //    @Update("update user set userPic=#{avatarUrl},update_time=now() where id=#{id}")
    void updateAvatar(String avatarUrl, Integer id);

    //    @Update("update user set userPassword = #{md5String},updateTime=now() where id = #{id}")
    void updatePassword(String md5String, Integer id);
}
