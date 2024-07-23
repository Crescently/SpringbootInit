package com.cre.springbootinit;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cre.springbootinit.mapper.UserMapper;
import com.cre.springbootinit.model.entity.User;
import com.cre.springbootinit.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class SpringbootInitApplicationTests {

    @Resource
    private UserMapper userMapper;

    @Test
    void TestMP() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = 1;
        User user = User.builder().id(id).username("刘震").userEmail("2376355613@qq.com").build();

//        userMapper.updateUserInfo(user);
        userMapper.update(user, new QueryWrapper<User>().eq("id", id));
    }

}
