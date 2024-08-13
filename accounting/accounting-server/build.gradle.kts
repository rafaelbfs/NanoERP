plugins {
  id("java")
  kotlin("jvm") version "2.0.0"
  id("war")
  id("io.openliberty.tools.gradle.Liberty") version "3.7"
}

group = "systems.terranatal.nanoerp.accounting"
version = rootProject.version

val jbExposedVer = "0.52.0"

repositories {
  mavenCentral()
}

tasks.test {
  useJUnitPlatform()
}

tasks.withType<JavaCompile>() {
  options.encoding = "UTF-8"
}

kotlin {
  jvmToolchain(21)
}

configurations.create("jdbcDriver") {
}

dependencies {
  //// CONTAINER
  compileOnly("jakarta.platform:jakarta.jakartaee-web-api:10.0.0")
  libertyRuntime("io.openliberty:openliberty-runtime:23.0.0.3")
  "jdbcDriver"("org.postgresql:postgresql:42.7.3")
  //compileOnly("org.jboss.logging:jboss-logging:3.6.0.Final")

  //// DATABASE & PERSISTENCE
  runtimeOnly("org.jetbrains.exposed:exposed-jdbc:$jbExposedVer")
  implementation("org.jetbrains.exposed:exposed-dao:$jbExposedVer")
  implementation("org.jetbrains.exposed:exposed-core:$jbExposedVer")
  implementation("org.postgresql:postgresql:42.7.3")

  //// GRPC & PROTOBUF
  implementation(libs.grpc.stubs.kotlin)
  implementation(libs.grpc.server.servlet)
  implementation(libs.protobuf.kotlin)

  //// OTHER SUBPROJECTS
  implementation(project(":accounting:accounting-protobuf"))

  //// TESTS
  testImplementation(platform("org.junit:junit-bom:5.10.0"))
  testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.create<Copy>("copyJdbcDriver") {
  from(configurations["jdbcDriver"])
  into("${layout.buildDirectory}/wlp/usr/servers/server/jdbc")
  include("*.jar")
}

tasks.test {
  useJUnitPlatform()
}