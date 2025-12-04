package com.hanserwei.admin.service.impl;

import com.hanserwei.admin.model.vo.user.FindUserInfoRspVO;
import com.hanserwei.admin.model.vo.user.UpdateAdminUserPasswordReqVO;
import com.hanserwei.admin.service.AdminUserService;
import com.hanserwei.common.domain.repository.UserRepository;
import com.hanserwei.common.enums.ResponseCodeEnum;
import com.hanserwei.common.exception.BizException;
import com.hanserwei.common.utils.Response;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Resource
    private UserRepository userRepository;
    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public Response<?> updatePassword(UpdateAdminUserPasswordReqVO updateAdminUserPasswordReqVO) {
        // 拿到用户名密码
        String username = updateAdminUserPasswordReqVO.getUsername();
        String password = updateAdminUserPasswordReqVO.getPassword();

        // 加密密码
        String encodePassword = passwordEncoder.encode(password);

        int updatedRows = userRepository.updatePasswordByUsername(username, encodePassword);

        if (updatedRows == 0) {
            // 如果更新行数为 0，说明用户名不存在
            throw new BizException(ResponseCodeEnum.USER_NOT_EXIST);
        }

        return Response.success();
    }

    @Override
    public Response<FindUserInfoRspVO> findUserInfo() {
        // 获取存储在 ThreadLocal 中的用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 拿到用户名
        String username = authentication.getName();
        return Response.success(new FindUserInfoRspVO(username));
    }
}
