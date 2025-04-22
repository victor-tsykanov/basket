import net.ltgt.gradle.errorprone.CheckSeverity
import net.ltgt.gradle.errorprone.errorprone

plugins {
    java
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.openapi.generator") version "7.12.0"
    id("com.google.protobuf") version "0.9.5"
    id("net.ltgt.errorprone") version "4.1.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

sourceSets {
    main {
        java {
            srcDir("${layout.buildDirectory.get()}/generated/openapi/src/main/java")
        }
        proto {
            srcDir("$rootDir/src/main/resources/contracts/")
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.6")
    implementation("org.openapitools:jackson-databind-nullable:0.2.6")
    implementation("io.grpc:grpc-stub:1.71.0")
    implementation("io.grpc:grpc-protobuf:1.71.0")
    implementation("io.grpc:grpc-netty:1.71.0")
    implementation("com.google.protobuf:protobuf-java:4.30.2")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("net.datafaker:datafaker:2.4.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.testcontainers:postgresql:1.20.6")
    testImplementation("org.testcontainers:junit-jupiter:1.20.6")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("io.github.classgraph:classgraph:4.8.179")
    testImplementation("org.mockito:mockito-core:5.16.1")
    testImplementation("io.rest-assured:spring-mock-mvc:5.5.1")
    errorprone("com.uber.nullaway:nullaway:0.12.4")
    implementation("org.jspecify:jspecify:1.0.0")
    errorprone("com.google.errorprone:error_prone_core:2.36.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.errorprone {
        check("NullAway", CheckSeverity.ERROR)
        check("RemoveUnusedImports", CheckSeverity.ERROR)
        option("NullAway:AnnotatedPackages", "com.example")
        disableWarningsInGeneratedCode = true
        excludedPaths = ".*/build/generated/.*"
    }
}

openApiGenerate {
    generatorName.set("spring")
    inputSpec.set("$rootDir/src/main/resources/contracts/basket/openapi.yaml")
    outputDir.set("${layout.buildDirectory.get()}/generated/openapi")
    apiPackage.set("com.example.basket.infrastructure.adapters.in.rest.api")
    modelPackage.set("com.example.basket.infrastructure.adapters.in.rest.dto")
    configOptions.put("useJakartaEe", "true")
    configOptions.put("interfaceOnly", "true")
    configOptions.put("delegatePattern", "true")
    configOptions.put("useTags", "true")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:4.30.2"
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.71.0"
        }
    }
    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                create("grpc")
            }
        }
    }
}
