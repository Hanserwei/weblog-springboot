plugins {
    `java-library`
}

group = "com.hanserwei.jwt"
version = project.parent?.version ?: "0.0.1-SNAPSHOT"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation(project(":weblog-module-common"))

    // jwt
    api(libs.jjwt.api)
    runtimeOnly(libs.jjwt.impl)
    runtimeOnly(libs.jjwt.jackson)
}
