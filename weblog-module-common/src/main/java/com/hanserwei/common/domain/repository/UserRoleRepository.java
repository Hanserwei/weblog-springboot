package com.hanserwei.common.domain.repository;

import com.hanserwei.common.domain.dataobject.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    List<UserRole> queryAllByUsername(String username);
}
