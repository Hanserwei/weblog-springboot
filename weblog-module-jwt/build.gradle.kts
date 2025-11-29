plugins {
    `java-library`
}

dependencies {
    api("org.springframework.boot:spring-boot-starter-security")
    implementation(project(":weblog-module-common"))
    implementation("org.apache.commons:commons-lang3:3.20.0")

    // jwt
    api(libs.jjwt.api)
    runtimeOnly(libs.jjwt.impl)
    runtimeOnly(libs.jjwt.jackson)
}
