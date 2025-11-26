plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    java
}

dependencies {

    // Spring Boot Web（示例）
    implementation("org.springframework.boot:spring-boot-starter-web")


    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // jsr380
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // 其他依赖…
    implementation(project(":weblog-module-common"))
    implementation(project(":weblog-module-admin"))
}
