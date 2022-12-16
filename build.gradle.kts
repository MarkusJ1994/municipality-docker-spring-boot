import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.5"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	war
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
	maven {
		url = uri("https://repository.openmindonline.it")
	}

}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	implementation("org.apache.poi:poi:5.2.0!!")
	implementation("org.apache.poi:poi-ooxml:5.2.0!!")

//	// https://mvnrepository.com/artifact/org.apache.batik/org.apache.batik.dom.svg
	implementation("org.apache.batik:org.apache.batik.dom.svg:1.6.0-20081006")
//	// https://mvnrepository.com/artifact/org.apache.batik/org.apache.batik.bridge
	implementation("org.apache.batik:org.apache.batik.bridge:1.6.0-20081006")
	// https://mvnrepository.com/artifact/org.apache.batik/org.apache.batik.svggen
	implementation("org.apache.batik:org.apache.batik.svggen:1.6.0-20081006")




//	implementation("org.jfree:org.jfree.svg:5.0.3")

	providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
