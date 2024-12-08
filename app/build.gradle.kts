plugins {
    application
    pmd
    jacoco
    id("com.gradleup.shadow") version "8.3.2"
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains:annotations:25.0.0")

    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("org.mockito:mockito-core:5.14.2")
    testImplementation("org.mockito:mockito-junit-jupiter:5.14.2")

    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("ch.qos.logback:logback-classic:1.5.12")

    implementation("com.sparkjava:spark-core:2.9.4")
    implementation("com.sparkjava:spark-template-freemarker:2.7.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.1")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

application {
    mainClass = "org.hsse.news.Application"
}

pmd {
    ruleSets = emptyList()
    ruleSetFiles = files("${projectDir}/src/main/resources/pmd/custom-ruleset.xml")
}


tasks.build {
    dependsOn(tasks.jacocoTestCoverageVerification)
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
            excludes = listOf(
                "org.hsse.news.Application",
                "org.hsse.news.api.SparkApplication",
            )

            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = BigDecimal.valueOf(0.6)
            }
        }
    }
}

version = "0.1"
