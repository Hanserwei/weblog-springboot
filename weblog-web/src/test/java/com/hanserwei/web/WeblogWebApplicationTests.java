package com.hanserwei.web;

import com.hanserwei.common.domain.dataobject.User;
import com.hanserwei.common.domain.repository.UserRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class WeblogWebApplicationTests {

    @Resource
    private UserRepository userRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void testLog() {
        log.info("这是一行 Info 级别日志");
        log.warn("这是一行 Warn 级别日志");
        log.error("这是一行 Error 级别日志");

        // 占位符
        String author = "Hanserwei";
        log.info("这是一行带有占位符日志，作者：{}", author);
    }

    @Test
    void insertTest() {
        User user = User.builder()
                .username("Hanserwei")
                .password("123456")
                .build();

        // 使用jpa插入数据
        User savedUser = userRepository.save(user);  // 保存并获取返回的实体
        userRepository.flush();  // 强制同步到数据库
    }
}
