plugins {
    kotlin("jvm")
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(projects.pbCoroutinesCore)
    implementation(projects.pbCoroutinesKafka)
}
