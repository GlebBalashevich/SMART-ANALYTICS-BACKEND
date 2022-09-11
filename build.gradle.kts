plugins {
    java
    id("org.springframework.boot") version "2.7.3"
}

apply(plugin = "io.spring.dependency-management")

group = "com.intexsoft"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks["jar"].enabled = false

tasks.named<Test>("test") {
    useJUnitPlatform()
}

val mapstructVersion = "1.4.2.Final"

dependencies{
    implementation("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")
    implementation("org.mapstruct:mapstruct:$mapstructVersion")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.r2dbc:r2dbc-postgresql:0.8.13.RELEASE")
    implementation("org.postgresql:postgresql")
}

