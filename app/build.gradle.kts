plugins {
    application
    pmd
    jacoco
    id("com.gradleup.shadow") version "8.3.2"
    id("java")
}

group = "org.hsse.news.parser"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))

    testImplementation("org.junit.jupiter:junit-jupiter")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("org.jsoup:jsoup:1.18.1")

    implementation("com.rometools:rome:2.1.0")

    implementation("org.slf4j:slf4j-api:2.0.0-alpha1")

    implementation("org.apache.logging.log4j:log4j-api:2.3")

    implementation("org.apache.logging.log4j:log4j-core:2.3")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            element = "CLASS"
            excludes = listOf("org.hsse.news.parser")

            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = BigDecimal.valueOf(0.8)
            }
        }
    }
}