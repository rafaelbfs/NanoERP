plugins {
    kotlin("jvm") version "2.0.0"
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "systems.terranatal.nanoerp.accounting"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("org.apache.poi:poi-ooxml:5.2.3")
    implementation("systems.terranatal.omnijfx:kfx:0.3.1-SNAPSHOT")
    implementation(project(":accounting:accounting-protobuf"))
    implementation(libs.grpc.stubs.kotlin)
    implementation(libs.protobuf.kotlin)
    implementation(libs.grpc.netty.shaded)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:1.8.1")

    compileOnly("org.apache.tomcat:annotations-api:6.0.53")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
javafx {
    version = "21.0.2"
    modules("javafx.controls", "javafx.graphics")
}