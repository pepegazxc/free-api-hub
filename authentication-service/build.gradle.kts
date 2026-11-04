plugins {
    java
    id("org.springframework.boot") version "4.1.1"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.github.pepega"
version = "0.0.1-SNAPSHOT"
description = "authentication-service"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // spring boot web
    implementation("org.springframework.boot:spring-boot-starter-web")

    // junit and testing
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    //security
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
        // jwt
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

    // lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    // spring boot validation
    implementation("org.springframework.boot:spring-boot-starter-validation")

    //spring boot jpa and db
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")
        // redis and lettuce
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.apache.commons:commons-pool2")

}

tasks.withType<Test> {
    useJUnitPlatform()
}
