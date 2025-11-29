package com.hanserwei.jwt.service;

import com.hanserwei.common.domain.dataobject.User;
import com.hanserwei.common.domain.dataobject.UserRole;
import com.hanserwei.common.domain.repository.UserRepository;
import com.hanserwei.common.domain.repository.UserRoleRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {

    @Resource
    private UserRepository userRepository;

    @Resource
    private UserRoleRepository userRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库查询用户信息
        User user = userRepository.getUsersByUsername(username);
        // 判断用户是否存在
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("用户不存在");
        }

        List<UserRole> userRoles = userRoleRepository.queryAllByUsername(username);
        // 转换为数组
        String[] roleArr = new String[0];
        if (!CollectionUtils.isEmpty(userRoles)) {
            roleArr = userRoles.stream()
                    .map(UserRole::getRoleName)
                    .toArray(String[]::new);
        }

        // authorities 用于指定角色，这里写死为 ADMIN 管理员
        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(roleArr)
                .build();
    }
}