import com.google.protobuf.gradle.id

plugins {
    kotlin("jvm") version "2.0.0"
    id("com.google.protobuf") version "0.9.4"
}

group = "systems.terranatal.nanoerp"
version = "0.1.0-SNAPSHOT"

val protobufVersion = rootProject.extra["versions.protobuf"]
val grpcVersion = rootProject.extra["versions.grpc"]

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.protobuf.kotlin)
    implementation(libs.grpc.stubs.kotlin)

    implementation(libs.grpc.protobuf)

    testImplementation(kotlin("test"))
}

protobuf {
    protoc {
        // The artifact spec for the Protobuf Compiler
        artifact = "com.google.protobuf:protoc:$protobufVersion"
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }
        create("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:${rootProject.extra["versions.protobuf.kotlin"]}:jdk8@jar"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                create("grpc")
                create("grpckt")
            }
            it.builtins {
                create("kotlin")
            }
        }
    }
}

tasks.test {
    useJUnitPlatform()
}