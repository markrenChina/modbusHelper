// Copyright 2000-2022 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
pluginManagement {
    val myProperties = java.util.Properties()
    val inputStream = file("C:/local/local.properties").inputStream()
    myProperties.load( inputStream )

    repositories {
//        google()
        gradlePluginPortal()
//        mavenCentral()
        maven {
            credentials {
                username= myProperties.getProperty("nexus.user")
                password = myProperties.getProperty("nexus.password")
            }
            isAllowInsecureProtocol = true
            url = uri(myProperties.getProperty("nexus.url"))
        }
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    plugins {
        kotlin("multiplatform").version(extra["kotlin.version"] as String)
        id("org.jetbrains.compose").version(extra["compose.version"] as String)
    }
}

rootProject.name = "client"

