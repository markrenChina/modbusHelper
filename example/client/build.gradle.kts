import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.zhipuchina"
version = "1.0-SNAPSHOT"


allprojects {
    val myProperties = Properties()
    val inputStream = file("C:/local/local.properties").inputStream()
    myProperties.load( inputStream )

    repositories {
        maven {
            credentials {
                username= myProperties.getProperty("nexus.user")
                password = myProperties.getProperty("nexus.password")
            }
            isAllowInsecureProtocol = true
            url = uri(myProperties.getProperty("nexus.url"))
        }
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}



kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("com.zhipuchina:modbusTcpHelper:1.0.0-dev4")
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "client"
            packageVersion = "1.0.0"
        }
    }
}
