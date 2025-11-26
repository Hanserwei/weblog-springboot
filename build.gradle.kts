plugins {
    java
    // Spring Boot 插件：声明，但不应用到父工程
    id("org.springframework.boot") version "3.5.8" apply false

    // Spring 的 dependency-management 插件：父工程和子工程都需要
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.hanserwei"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

allprojects {
    group = "com.hanserwei"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {

    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")

    java.sourceCompatibility = JavaVersion.VERSION_21
    java.targetCompatibility = JavaVersion.VERSION_21

    // ========= 统一版本管理（类似 Maven dependencyManagement）=========
    dependencyManagement {
        imports {
            // Spring Boot 官方 BOM（最重要，统一管理绝大部分依赖版本）
            mavenBom("org.springframework.boot:spring-boot-dependencies:3.5.8")
        }
    }

    dependencies {

        // Lombok (compileOnly + annotationProcessor)
        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")

        // 如果你的测试也使用 Lombok
        testCompileOnly("org.projectlombok:lombok")
        testAnnotationProcessor("org.projectlombok:lombok")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
