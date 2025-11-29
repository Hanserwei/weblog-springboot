plugins {
    java
}

dependencies {
    // Spring Security
    implementation("org.springframework.boot:spring-boot-starter-security")
    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation(project(":weblog-module-common"))

    implementation(project(":weblog-module-jwt"))
    testImplementation("org.springframework.security:spring-security-test")
}