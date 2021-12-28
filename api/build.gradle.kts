import org.gradle.kotlin.dsl.flyway
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.0-SNAPSHOT"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("com.expediagroup.graphql") version "5.1.0"
	id("org.flywaydb.flyway") version "8.0.1"
	kotlin("jvm") version "1.6.0"
	kotlin("plugin.spring") version "1.6.0"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
}

group = "com.okeicalm"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
}

val graphqlKotlinVersion = "5.1.0"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-jooq")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("com.expediagroup", "graphql-kotlin-spring-server", graphqlKotlinVersion)
	implementation("com.expediagroup", "graphql-kotlin-schema-generator", graphqlKotlinVersion)
	implementation("org.flywaydb:flyway-core")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("mysql:mysql-connector-java")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

flyway {
	url = System.getenv("MYSQL_URL")
	user = System.getenv("MYSQL_USER")
	password = System.getenv("MYSQL_PASSWORD")
}

graphql {
	schema {
		packages = listOf("com.okeicalm.simpleJournalEntry")
	}
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

val graphqlGenerateSDL by tasks.getting(com.expediagroup.graphql.plugin.gradle.tasks.GraphQLGenerateSDLTask::class) {
	packages.set(listOf("com.okeicalm.simpleJournalEntry"))
	schemaFile.set(file("${project.projectDir}/schema.graphql"))
}
