package com.cre.springbootinit.controller;

import com.cre.springbootinit.common.BaseResponse;
import com.cre.springbootinit.common.ErrorCode;
import com.cre.springbootinit.exception.BusinessException;
import com.cre.springbootinit.model.request.admin.UpdateUserRoleRequest;
import com.cre.springbootinit.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Resource
    private UserService userService;

    // 更改用户身份信息的方法
    @PutMapping("/updateUserRole")
    public BaseResponse<?> updateUserRole(@RequestBody UpdateUserRoleRequest updateUserRoleRequest) {
        if (updateUserRoleRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        userService.updateUserRole( updateUserRoleRequest);
        return BaseResponse.success();

    }
}
