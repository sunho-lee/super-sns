plugins {
    java
    id("org.springframework.boot") version "3.1.4"
    id("io.spring.dependency-management") version "1.1.3"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

val queryDslVersion = "5.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}


repositories {
    mavenCentral()
}
val asciidoctorExt: Configuration by configurations.creating


configurations {
    all {
        exclude("org.springframework.boot", "spring-boot-starter-logging")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-core")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("org.flywaydb:flyway-mysql")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
    implementation("org.projectlombok:lombok-mapstruct-binding:0.2.0")

    runtimeOnly("com.mysql:mysql-connector-j")

    implementation("com.querydsl:querydsl-jpa:${queryDslVersion}:jakarta")
    annotationProcessor("com.querydsl:querydsl-apt:${queryDslVersion}:jakarta")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("org.springframework.security:spring-security-test")

    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mysql")

    "asciidoctorExt"("org.springframework.restdocs:spring-restdocs-asciidoctor")
}

val querydslDir = "src/main/generated"

sourceSets {
    getByName("main").java.srcDirs(querydslDir)
}

tasks.withType<JavaCompile> {
    options.generatedSourceOutputDirectory = file(querydslDir)
}

tasks.named("clean") {
    doLast {
        file(querydslDir).deleteRecursively()
    }
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

tasks.bootBuildImage {


}