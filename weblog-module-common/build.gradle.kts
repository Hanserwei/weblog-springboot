plugins {
    `java-library`
}

dependencies {

    // guava
    implementation("com.google.guava:guava:33.5.0-jre")
    // commons-lang3
    implementation("org.apache.commons:commons-lang3:3.20.0")
    // jpa
    api("org.springframework.boot:spring-boot-starter-data-jpa")
    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    // jackson
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.core:jackson-core")
    // aop
    implementation("org.springframework.boot:spring-boot-starter-aop")
    // web
    implementation("org.springframework.boot:spring-boot-starter-web")
    // postgresql
    runtimeOnly("org.postgresql:postgresql")
}
