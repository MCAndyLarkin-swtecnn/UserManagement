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

    //Database
    // https://mvnrepository.com/artifact/org.jdbi/jdbi3-core
    implementation("org.jdbi:jdbi3-core:3.34.0")
    // https://mvnrepository.com/artifact/org.jdbi/jdbi3-sqlobject
    implementation("org.jdbi:jdbi3-sqlobject:3.34.0")
    // https://mvnrepository.com/artifact/com.h2database/h2
    implementation("com.h2database:h2:2.1.214")
    // https://mvnrepository.com/artifact/io.dropwizard/dropwizard-db
    implementation("io.dropwizard:dropwizard-db:2.1.4")
    // https://mvnrepository.com/artifact/io.dropwizard/dropwizard-jdbi3
    implementation("io.dropwizard:dropwizard-jdbi3:2.1.4")


    //Utils
    // https://mvnrepository.com/artifact/com.github.rkpunjal.sqlsafe/sql-injection-safe
    implementation("com.github.rkpunjal.sqlsafe:sql-injection-safe:1.0.2")

    //Swagger
    // https://mvnrepository.com/artifact/com.smoketurner/dropwizard-swagger
    implementation("com.smoketurner:dropwizard-swagger:2.0.12-1")

    //Tests
    // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
    // https://mvnrepository.com/artifact/io.mockk/mockk
    testImplementation("io.mockk:mockk:1.13.2")
    // https://mvnrepository.com/artifact/io.dropwizard/dropwizard-testing
    testImplementation("io.dropwizard:dropwizard-testing:2.1.4")


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