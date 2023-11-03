plugins {
    java
    id("org.springframework.boot") version "3.1.4"
    id("io.spring.dependency-management") version "1.1.3"
    //id("org.asciidoctor.jvm.convert") version "3.3.2"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}


repositories {
    mavenCentral()
}

// Ascii Doc Snippet Directory
//val snippetsDir by extra { file("build/generated-snippets") }
//val asciidoctorExt: Configuration by configurations.creating

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-graphql")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-hateoas")
    //implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

    runtimeOnly("com.mysql:mysql-connector-j")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework:spring-webflux")
    testImplementation("org.springframework.graphql:spring-graphql-test")


//    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
//    asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor")
}

/*
// Ascii Doc Create Tasks
tasks {
    // Test 결과를 snippet Directory에 출력
    withType<Test> {
        outputs.dir(snippetsDir)
    }

    test {
        useJUnitPlatform()
    }

    "asciidoctor" {
        // test 가 성공해야만, 아래 Task 실행
        dependsOn(test)
        // 기존에 존재하는 Docs 삭제(문서 최신화를 위해)
        doFirst {
            delete(file("src/main/resources/static/docs"))
        }
        // snippet Directory 설정
        inputs.dir(snippetsDir)
        // Ascii Doc 파일 생성
        doLast {
            copy {
                from("build/docs/asciidoc")
                into("src/main/resources/static/docs")
            }
        }
    }

    build {
        // Ascii Doc 파일 생성이 성공해야만, Build 진행
        dependsOn(asciidoctorExt)
    }
}
*/

tasks.test {
    useJUnitPlatform()
}


