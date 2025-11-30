plugins {
    `java-library`
}

dependencies {
    api("org.springframework.boot:spring-boot-starter-security")

    implementation(project(":weblog-module-common"))

    api(project(":weblog-module-jwt"))
    implementation("org.springframework.boot:spring-boot-starter-validation")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}
