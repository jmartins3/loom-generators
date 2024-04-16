plugins {
    kotlin("jvm") version "1.9.20"
    id("java")

}

group = "pt.isel.pc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("--add-exports=java.base/jdk.internal.vm=ALL-UNNAMED")
}

tasks.withType<Test>().configureEach {
    jvmArgs(listOf(
        "--add-exports", "java.base/jdk.internal.vm=ALL-UNNAMED"
    ))
}

tasks.withType<JavaExec>().configureEach {
    jvmArgs(listOf(
        "--add-exports", "java.base/jdk.internal.vm=ALL-UNNAMED"
    ))
}

tasks.test {
    useJUnitPlatform()
    jvmArgs(listOf(
        "--add-exports", "java.base/jdk.internal.vm=ALL-UNNAMED"
    ))
}

kotlin {
    jvmToolchain(21)
}