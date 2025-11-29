package com.hanserwei.common.domain.repository;

import com.hanserwei.common.domain.dataobject.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User getUsersByUsername(String username);
}
