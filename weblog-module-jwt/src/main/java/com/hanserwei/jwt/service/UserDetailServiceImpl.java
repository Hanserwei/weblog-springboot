package com.hanserwei.jwt.service;

import com.hanserwei.common.domain.dataobject.User;
import com.hanserwei.common.domain.repository.UserRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {

    @Resource
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库查询用户信息
        User user = userRepository.getUsersByUsername(username);
        // 判断用户是否存在
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("用户不存在");
        }

        // authorities 用于指定角色，这里写死为 ADMIN 管理员
        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("ADMIN")
                .build();
    }
}