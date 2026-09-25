plugins {
    kotlin("multiplatform") version "2.0.0"
}

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        withJava()
        mainRun {
            mainClass.set("com.newsfeed.MainKt")
        }
    }

    // Bisa ditambahkan target lain jika diinginkan, misal: js(IR), iosX64(), iosArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
            }
        }
        val jvmMain by getting {
            dependencies {
                // jvm specific dependencies jika ada
            }
        }
        val jvmTest by getting
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
