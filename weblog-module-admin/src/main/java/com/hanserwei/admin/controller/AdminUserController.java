package com.hanserwei.admin.controller;

import com.hanserwei.admin.model.vo.FindUserInfoRspVO;
import com.hanserwei.admin.model.vo.UpdateAdminUserPasswordReqVO;
import com.hanserwei.admin.service.AdminUserService;
import com.hanserwei.common.aspect.ApiOperationLog;
import com.hanserwei.common.utils.Response;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端用户控制器
 */
@RestController
@RequestMapping("/admin")
public class AdminUserController {

    @Resource
    private AdminUserService adminUserService;

    /**
     * 修改用户密码
     */
    @PostMapping("/password/update")
    @ApiOperationLog(description = "修改用户密码")
    public Response<?> updatePassword(@RequestBody @Validated UpdateAdminUserPasswordReqVO updateAdminUserPasswordReqVO) {
        return adminUserService.updatePassword(updateAdminUserPasswordReqVO);
    }

    /**
     * 获取用户信息
     */
    @PostMapping("/user/info")
    @ApiOperationLog(description = "获取用户信息")
    public Response<FindUserInfoRspVO> findUserInfo() {
        return adminUserService.findUserInfo();
    }
}
