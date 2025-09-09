plugins {
    kotlin("multiplatform") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.0"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
    id("io.gitlab.arturbosch.detekt") version "1.23.6"
    id("org.jetbrains.dokka") version "1.9.20" // Per la documentazione
    id("org.danilopianini.git-sensitive-semantic-versioning") version "2.0.5" // Per versioning basato su git
}

detekt {
    source.setFrom(
        files(
            "src/linuxX64Main",
            "src/macosArm64Main",
            "src/macosX64Main",
            "src/mingwX64Main",
            "src/commonMain",
        ),
    )

    config.setFrom(files("$projectDir/detekt.yml"))
    baseline = file("$projectDir/config/baseline.xml")
    debug = true
}

group = "org.keyla"
version = "1.0-SNAPSHOT"
val appName = "keyla"
repositories {
    mavenCentral()
}

dependencies {
    "dokkaPlugin"("org.jetbrains.dokka:kotlin-as-java-plugin:1.9.20")
}

tasks.register("installGitHooks") {
    description = "Install git hooks from scripts folder"
    group = "git hooks"

    doLast {
        val scriptsDir = file("${project.rootDir}/scripts")
        val hooksDir = file("${project.rootDir}/.git/hooks")

        if (!scriptsDir.exists()) {
            throw GradleException("Scripts directory not found at ${scriptsDir.absolutePath}")
        }

        hooksDir.mkdirs()

        // Copy pre-commit hook
        val preCommitSource = file("$scriptsDir/pre-commit")
        val preCommitTarget = file("$hooksDir/pre-commit")

        if (preCommitSource.exists()) {
            preCommitSource.copyTo(preCommitTarget, overwrite = true)
            preCommitTarget.setExecutable(true)
            println("Installed pre-commit hook")
        } else {
            println("Pre-commit hook not found in scripts folder")
        }

        // Copy commit-msg hook
        val commitMsgSource = file("$scriptsDir/commit-msg")
        val commitMsgTarget = file("$hooksDir/commit-msg")

        if (commitMsgSource.exists()) {
            commitMsgSource.copyTo(commitMsgTarget, overwrite = true)
            commitMsgTarget.setExecutable(true)
            println("Installed commit-msg hook")
        } else {
            println("Commit-msg hook not found in scripts folder")
        }

        println("Git hooks installation completed!")
    }
}

// Task to format all code
tasks.register("formatCode") {
    group = "code quality"
    description = "Format all Kotlin files in the project"
    dependsOn("ktlintFormat")

    doLast {
        println("All Kotlin files have been formatted!")
    }
}

// Task to check all code
tasks.register("checkCode") {
    group = "code quality"
    description = "Run all code quality checks"
    dependsOn("ktlintCheck", "detekt")

    doLast {
        println("All code quality checks completed!")
    }
}

tasks.register("ciCheckCode") {
    group = "verification"
    description = "Runs all code quality checks with CI-specific configurations"

    doFirst {
        (ktlint.filter { exclude("**/build/**", "**/.gradle/**", "**/generated/**") })
        detekt.source.setFrom(
            files(
                "src/linuxX64Main",
                "src/macosArm64Main",
                "src/macosX64Main",
                "src/mingwX64Main",
                "src/commonMain",
            ),
        )
    }

    dependsOn("ktlintCheck", "detekt")
}

// Task for complete automatic fix
tasks.register("fixCode") {
    group = "code quality"
    description = "Automatically fix all resolvable style issues"
    dependsOn("ktlintFormat")

    doLast {
        println("Code formatted and style issues automatically resolved!")
        println("Run 'gradlew checkCode' to verify any remaining issues")
    }
}

// Task to run pre-commit checks manually
tasks.register("preCommitCheck") {
    group = "git hooks"
    description = "Run the same checks as the pre-commit hook"
    dependsOn("ktlintCheck", "detekt", "allTests")

    doLast {
        println("All pre-commit checks passed!")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

kotlin {
    jvm()
    listOf(
        // Linux
        linuxX64(),
        // Windows
        mingwX64(),
        // Mac M1
        macosArm64(),
        // Mac Legacy
        macosX64(),
    ).forEach { nativeTarget ->
        nativeTarget.apply {
            binaries {
                executable {
                    entryPoint = "org.keyla.main"
                    baseName = "Keyla"
                }
            }
        }
    }

    kotlin.applyDefaultHierarchyTemplate()

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
                // Usa WinHttp invece di curl per Windows
                implementation("io.ktor:ktor-client-winhttp:$ktorVersion")
            }
        }

        val macosArm64Main by getting {
            dependencies {
                implementation("io.ktor:ktor-client-darwin:$ktorVersion")
            }
        }

        val macosX64Main by getting {
            dependencies {
                implementation("io.ktor:ktor-client-darwin:$ktorVersion")
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
