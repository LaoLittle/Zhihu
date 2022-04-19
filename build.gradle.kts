plugins {
    val kotlinVersion = "1.6.20"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.10.1"
}

group = "org.laolittle.plugin"
version = "1.0"

repositories {
    maven("https://maven.aliyun.com/repository/central")
    maven("https://packages.jetbrains.team/maven/p/skija/maven")
    mavenCentral()
}

dependencies {
    val ktorVer = "2.0.0"
    implementation("io.ktor:ktor-client-core-jvm:$ktorVer")
    implementation("io.ktor:ktor-client-okhttp:$ktorVer")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVer")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVer")
    //implementation("org.jsoup:jsoup:1.14.3")
    testImplementation(kotlin("test"))
}