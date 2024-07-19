package com.cre.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cre.springbootinit.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {


}
