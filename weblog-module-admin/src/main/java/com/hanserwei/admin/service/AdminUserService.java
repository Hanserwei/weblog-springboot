package com.hanserwei.admin.service;

import com.hanserwei.admin.model.vo.user.FindUserInfoRspVO;
import com.hanserwei.admin.model.vo.user.UpdateAdminUserPasswordReqVO;
import com.hanserwei.common.utils.Response;

public interface AdminUserService {
    /**
     * 修改密码
     * @param updateAdminUserPasswordReqVO 修改密码参数
     * @return 修改密码结果
     */
    Response<?> updatePassword(UpdateAdminUserPasswordReqVO updateAdminUserPasswordReqVO);

    /**
     * 获取当前登录用户信息
     * @return 当前登录用户信息
     */
    Response<FindUserInfoRspVO> findUserInfo();
}
