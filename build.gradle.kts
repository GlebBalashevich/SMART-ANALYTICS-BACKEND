import com.avast.gradle.dockercompose.tasks.ComposeUp

plugins {
    java
    id("org.springframework.boot") version "2.7.3"
    id("com.avast.gradle.docker-compose") version "0.16.9"
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

tasks.named<ComposeUp>("composeUp") {
    dependsOn("build")
}

val mapstructVersion = "1.5.2.Final"

dependencies{

    //Common Dependencies
    implementation("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")
    implementation("org.mapstruct:mapstruct:$mapstructVersion")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    //Security Dependencies
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt:0.9.1")
    implementation("javax.xml.bind:jaxb-api:2.3.1")

    //Database Dependencies
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("io.r2dbc:r2dbc-postgresql:0.8.13.RELEASE")
    implementation("org.postgresql:postgresql")

    //Swagger Dependencies
    implementation("org.springdoc:springdoc-openapi-webflux-ui:1.6.11")

    //Test Dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("commons-io:commons-io:2.11.0")
}

