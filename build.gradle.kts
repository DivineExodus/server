plugins {
    id("java")
    id("application")
    id("org.sonarqube") version "4.4.1.3373"
    id("jacoco-report-aggregation")
}

group = "fr.divineexodus"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("fr.divineexodus.DivineExodus")
}

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    // Minestom - base
    implementation("dev.hollowcube:minestom-ce:dev") // using self-built forked version of Minestom for 1.20.4

    // Configuration
    implementation("com.electronwill.night-config:toml:3.6.0")

    // Testing
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

sonar {
    println("Using ${System.getenv("SONAR_HOST_URL")} as host")
    properties {
        property("sonar.projectKey", "DivineExodus_server_AYxTmxkxY2D0l8qiaVAK")
    }
}

tasks.jacocoTestReport {
    reports {
        xml.required = true
        csv.required = false
        html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
    }
}

tasks.check {
    dependsOn(tasks.named<JacocoReport>("testCodeCoverageReport"))
}