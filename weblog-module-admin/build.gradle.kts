plugins {
    java
}

dependencies {
    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation(project(":weblog-module-common"))
}