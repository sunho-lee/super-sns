plugins {
    java
    id("org.springframework.boot") version "3.1.4"
    id("io.spring.dependency-management") version "1.1.3"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}


repositories {
    mavenCentral()
}
val asciidoctorExt: Configuration by configurations.creating


configurations {
    all {
        exclude("org.springframework.boot","spring-boot-starter-logging")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-graphql")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-hateoas")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("org.springframework.boot:spring-boot-starter-security")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
    implementation("org.projectlombok:lombok-mapstruct-binding:0.2.0")

    runtimeOnly("com.mysql:mysql-connector-j")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework:spring-webflux")
    testImplementation("org.springframework.graphql:spring-graphql-test")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("org.springframework.security:spring-security-test")

    "asciidoctorExt"("org.springframework.restdocs:spring-restdocs-asciidoctor")
}


val snippetsDir = file("build/generated-snippets")

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
    outputs.dir(snippetsDir)
}

tasks.asciidoctor {
    //val test by tasks
    dependsOn(tasks.test)
    configurations("asciidoctorExt")
    baseDirFollowsSourceFile()
    inputs.dir(snippetsDir)
}

tasks.register("copyDocument", Copy::class) {
    dependsOn(tasks.asciidoctor)
    from(file("build/docs/asciidoc/api-docs.html"))
    into(file("src/main/resources/static/docs"))
}

tasks.build {
    dependsOn(tasks.getByName("copyDocument"))
}

tasks.bootJar {
    dependsOn(tasks.getByName("copyDocument"))
}

tasks.jar {
    enabled = false
}