import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Workaround for idea do not recognize open classes by kotlin-spring, remove buildscript-block after fixed
// https://youtrack.jetbrains.com/issue/KT-36331
buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.31")
    }
}

plugins {
	id("org.springframework.boot") version "2.4.3"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.4.31"
	kotlin("plugin.spring") version "1.4.31"
    kotlin("plugin.jpa") version "1.4.31"
}

group = "io.utu"
version = "1.0"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
    // Spring & kotlin
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-allopen:1.4.31")
    implementation("org.jetbrains.kotlin:kotlin-noarg:1.4.31")

    // GraphQL
    implementation("com.graphql-java-kickstart:graphql-spring-boot-starter:11.0.0")
    implementation("com.zhokhov.graphql:graphql-datetime-spring-boot-starter:3.1.0")
    implementation("com.graphql-java-kickstart:graphql-java-tools:11.0.0")

    // Database
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql:42.2.1")
    implementation("com.vladmihalcea:hibernate-types-52:2.4.0")

    implementation("org.flywaydb:flyway-core")

    // Testing
	testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.graphql-java-kickstart:graphql-spring-boot-starter-test:11.0.0")
    testImplementation("org.assertj:assertj-core:3.19.0")
    testImplementation("org.testcontainers:junit-jupiter:1.15.1")
    testImplementation("org.testcontainers:postgresql:1.15.1")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    this.archiveFileName.set("project-boot.jar")
}
