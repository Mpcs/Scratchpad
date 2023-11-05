import org.apache.tools.ant.taskdefs.Java

plugins {
    id("java")
}

group = "com.mpcs"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(mapOf("path" to ":Logging")))
    implementation(project(mapOf("path" to ":Utility")))
    implementation(project(mapOf("path" to ":Utility")))
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    annotationProcessor(project(":Config"))
    implementation(project(":Config"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile>(){
    doFirst {
        println("AnnotationProcessorPath for $name is ${options.annotationProcessorPath?.files}")
    }
}