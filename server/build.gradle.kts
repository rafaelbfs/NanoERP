plugins {
    kotlin("jvm") version "2.0.0"
}

repositories {
    maven {
        url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
    }
}

group = "systems.terranatal.omnijfx"
version = rootProject.version

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":grpc"))

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)

    implementation(libs.grpc.stubs.kotlin)
    implementation(libs.grpc.server.netty)

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}