plugins {
    id("java")
}

group = "systems.terranatal.nanoerp"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":accounting:accounting-server"))

    implementation("org.glassfish.main.extras:glassfish-embedded-web:7.0.12")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}