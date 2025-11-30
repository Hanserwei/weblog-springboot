package com.hanserwei.common.domain.repository;

import com.hanserwei.common.domain.dataobject.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {
    User getUsersByUsername(String username);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = :newPassword WHERE u.username = :username")
    int updatePasswordByUsername(@Param("username") String username,
                                 @Param("newPassword") String newPassword);
}
