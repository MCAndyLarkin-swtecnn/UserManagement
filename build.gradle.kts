import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    application
}

group = "me.maksim"
version = "1.0-SNAPSHOT"

apply {
    plugin("application")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    //REST
    val dropWizardVersion = "2.0.34"
    // https://mvnrepository.com/artifact/io.dropwizard/dropwizard-core
    implementation("io.dropwizard:dropwizard-core:$dropWizardVersion")
    // https://mvnrepository.com/artifact/io.dropwizard/dropwizard-assets
    implementation("io.dropwizard:dropwizard-assets:$dropWizardVersion")

    //DI
    // https://mvnrepository.com/artifact/org.kodein.di/kodein-di-generic-jvm
    implementation("org.kodein.di:kodein-di-generic-jvm:6.4.0")


    //Tests
    // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
    // https://mvnrepository.com/artifact/io.mockk/mockk
    testImplementation("io.mockk:mockk:1.13.2")

}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClass.set("UsersManagementApp")
}