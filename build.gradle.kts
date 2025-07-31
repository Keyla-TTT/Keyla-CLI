plugins {
    kotlin("multiplatform") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.0"
}

group = "org.keyla"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

kotlin {
    jvm()
    listOf(
        linuxX64(), // Linux
        mingwX64(), // Windows
        macosArm64(), // Mac M1
        macosX64(), // Mac Legacy
    ).forEach { nativeTarget ->
        nativeTarget.apply {
            binaries {
                executable {
                    entryPoint = "org.keyla.main" // Point to the main function
                }
            }
        }
    }
    macosX64 {
        compilations.getByName("main") {
            cinterops.create("libcurl") {
                defFile(project.file("src/nativeInterop/cinterop/libcurl.def"))
            }
        }
    }
    macosArm64 {
        compilations.getByName("main") {
            cinterops.create("libcurl") {
                defFile(project.file("src/nativeInterop/cinterop/libcurl.def"))
            }
        }
    }

    sourceSets {
        val ktorVersion = "2.3.7"
        val kotterVersion = "1.2.1"
        val coroutinesVersion = "1.7.3"
        val serializationVersion = "1.7.3"

        val commonMain by getting {
            dependencies {
                implementation("com.varabyte.kotter:kotter:$kotterVersion")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-cio:$ktorVersion")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("io.ktor:ktor-client-mock:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
            }
        }

        val linuxX64Main by getting {
            dependencies {
                implementation("io.ktor:ktor-client-curl:$ktorVersion")
            }
        }

        val mingwX64Main by getting {
            dependencies {
                implementation("io.ktor:ktor-client-curl:$ktorVersion")
            }
        }

        val macosArm64Main by getting {
            dependencies {
                implementation("io.ktor:ktor-client-curl:$ktorVersion")
            }
        }

        val macosX64Main by getting {
            dependencies {
                implementation("io.ktor:ktor-client-curl:$ktorVersion")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation("com.varabyte.kotterx:kotter-test-support:$kotterVersion")
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
            }
        }
    }
}

val appName = "keyla"
