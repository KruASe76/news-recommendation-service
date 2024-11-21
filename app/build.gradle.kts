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
    dependencies {
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    
    compileOnly("org.jetbrains:annotations:25.0.0")

    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("ch.qos.logback:logback-classic:1.5.12")

    implementation("org.jsoup:jsoup:1.18.1")
    implementation("com.rometools:rome:2.1.0")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

application {
    mainClass = "org.hsse.news.App"
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
            excludes = listOf("org.hsse.news.App")

            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = BigDecimal.valueOf(0.8)
            }
        }
    }
}
