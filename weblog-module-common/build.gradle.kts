plugins {
    java
}

dependencies {

    // guava
    implementation("com.google.guava:guava:33.5.0-jre")
    // commons-lang3
    implementation("org.apache.commons:commons-lang3:3.20.0")
    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    // jackson
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.core:jackson-core")
    // aop
    implementation("org.springframework.boot:spring-boot-starter-aop")
}
