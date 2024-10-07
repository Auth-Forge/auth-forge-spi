plugins {
    id("java")
    id("io.spring.dependency-management") version "1.1.6"
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
