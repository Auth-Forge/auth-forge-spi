import org.gradle.api.publish.maven.MavenPublication

plugins {
    id("java")
    id("io.spring.dependency-management") version "1.1.6"
    id("maven-publish")
}

group = "io.authforge"
version = project.findProperty("version")?.toString() ?: "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val junitVersion = "5.10.0"
val keycloakVersion = "22.0.3"

dependencyManagement {
    imports {
        mavenBom("org.junit:junit-bom:$junitVersion")
        mavenBom("org.keycloak:keycloak-parent:$keycloakVersion")
    }
}

dependencies {
    compileOnly("org.keycloak:keycloak-core")
    compileOnly("org.keycloak:keycloak-server-spi")
    compileOnly("org.keycloak:keycloak-server-spi-private")
    compileOnly("org.keycloak:keycloak-services")
    compileOnly("org.jboss.logging:jboss-logging")

    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    archiveBaseName.set(project.name)
}

tasks.withType<JavaCompile> {
    sourceCompatibility = JavaVersion.VERSION_17.toString()
    targetCompatibility = JavaVersion.VERSION_17.toString()
}

// Custom versioning strategy for snapshots
publishing {
    publications {
        create<MavenPublication>("authForgeSpi") {
            from(components["java"])

            groupId = "io.authforge"
            artifactId = "auth-forge-spi"
            version = project.version.toString()

            // Customize the snapshot versioning
            versionMapping {
                allVariants {
                    fromResolutionResult()
                }
            }
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Auth-Forge/auth-forge-spi")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
