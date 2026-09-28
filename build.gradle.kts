plugins {
    java
    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.gkcontas"
version = "0.1.0-SNAPSHOT"
description = "Catalogue of the GoF design patterns, with where Spring uses each one and when not to reach for it"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

// One build, twenty-two browsable directories.
//
// The layout is deliberate. Twenty-two Gradle projects, each with its own wrapper and
// build file, would be pure ceremony: the value here is the pattern code, not repeating
// build configuration. But a single conventional src/main/java tree would bury each
// pattern four levels down and put its README somewhere else entirely — and being able
// to open behavioral/strategy/ and see the explanation next to the implementation is
// most of what makes a catalogue useful.
//
// So every pattern directory is its own source root, holding main/ and test/ with the
// usual package tree inside. The IDE understands it, javac gets directories that match
// the packages, and the repository stays navigable.
sourceSets {
    main {
        java.setSrcDirs(listOf(
        "behavioral/chain-of-responsibility/main",
        "behavioral/command/main",
        "behavioral/iterator/main",
        "behavioral/mediator/main",
        "behavioral/memento/main",
        "behavioral/observer/main",
        "behavioral/state/main",
        "behavioral/strategy/main",
        "behavioral/template-method/main",
        "behavioral/visitor/main",
        "creational/abstract-factory/main",
        "creational/builder/main",
        "creational/factory-method/main",
        "creational/prototype/main",
        "creational/singleton/main",
        "structural/adapter/main",
        "structural/bridge/main",
        "structural/composite/main",
        "structural/decorator/main",
        "structural/facade/main",
        "structural/flyweight/main",
        "structural/proxy/main",
        ))
        resources.setSrcDirs(listOf("src/main/resources"))
    }
    test {
        java.setSrcDirs(listOf(
        "behavioral/chain-of-responsibility/test",
        "behavioral/command/test",
        "behavioral/iterator/test",
        "behavioral/mediator/test",
        "behavioral/memento/test",
        "behavioral/observer/test",
        "behavioral/state/test",
        "behavioral/strategy/test",
        "behavioral/template-method/test",
        "behavioral/visitor/test",
        "creational/abstract-factory/test",
        "creational/builder/test",
        "creational/factory-method/test",
        "creational/prototype/test",
        "creational/singleton/test",
        "structural/adapter/test",
        "structural/bridge/test",
        "structural/composite/test",
        "structural/decorator/test",
        "structural/facade/test",
        "structural/flyweight/test",
        "structural/proxy/test",
        ))
        resources.setSrcDirs(listOf("src/test/resources"))
    }
}

dependencies {
    // The patterns are plain Java; Spring is here because several of them are explained
    // by pointing at where the framework already uses them, and the observer example is
    // built on ApplicationEventPublisher.
    implementation("org.springframework.boot:spring-boot-starter")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// Nothing to run: this is a library of examples exercised by its tests.
tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}
tasks.named<Jar>("jar") {
    enabled = true
}
