plugins {
    java
    id("org.springframework.boot") version "3.5.4"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "org.example"
version = "0.0.1-SNAPSHOT"
description = "TestOomException"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// ==== Таска для OpenShift S2I ====
val copyJar by tasks.registering(Copy::class) {
    dependsOn(tasks.named("build"))
    from(layout.buildDirectory.dir("libs")) {
        include("TestOomException-0.0.1-SNAPSHOT.jar")
        exclude("*-sources.jar", "*-javadoc.jar")
    }
    into(System.getenv("DEPLOYMENTS_DIR") ?: "/deployments")
}

// Чтобы таска выполнялась всегда после build
tasks.named("build") {
    finalizedBy(copyJar)
}