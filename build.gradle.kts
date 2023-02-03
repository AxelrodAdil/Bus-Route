plugins {
    java
    id("org.springframework.boot") version "3.0.2"
    id("io.spring.dependency-management") version "1.1.0"
}

group = "kz.axelrod"
version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("redis.clients:jedis:4.3.1")
    implementation("org.springframework.data:spring-data-redis:3.0.1")
    implementation("io.swagger.core.v3:swagger-annotations:2.1.6")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.6")

    annotationProcessor("org.projectlombok:lombok:1.18.22")
    compileOnly("org.projectlombok:lombok:1.18.22")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
