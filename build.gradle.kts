plugins {
    kotlin("multiplatform") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.0"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
    id("io.gitlab.arturbosch.detekt") version "1.23.6"
    id("org.jetbrains.dokka") version "1.9.20" // Per la documentazione
    id("org.danilopianini.git-sensitive-semantic-versioning") version "2.0.5" // Per versioning basato su git
}

// Configurazione ktlint per escludere directory build
ktlint {
    filter {
        exclude("**/build/**")
        exclude("**/generated/**")
    }
}

// Configurazione detekt per escludere directory build
detekt {
    buildUponDefaultConfig = true
    source = files("src/")
}

group = "org.keyla"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    "dokkaPlugin"("org.jetbrains.dokka:kotlin-as-java-plugin:1.9.20")
}

tasks.register("installGitHooks") {
    description = "Install git hooks (pre-commit and commit-msg) compatible with Windows"
    group = "git hooks"
    doLast {
        // Pre-commit hook
        val preCommitFile = file("${project.rootDir}/.git/hooks/pre-commit")
        preCommitFile.parentFile.mkdirs()
        preCommitFile.writeText(
            """
            #!/usr/bin/env sh
            echo "Running ktlint and detekt checks..."

            # Detect Windows and use appropriate command
            if [ -f "./gradlew.bat" ] && [ "${'$'}OSTYPE" = "win32" -o "${'$'}OSTYPE" = "msys" -o "${'$'}OSTYPE" = "cygwin" ]; then
                cmd.exe /c gradlew.bat ktlintCheck detekt
                EXIT_CODE=${'$'}?
            else
                ./gradlew ktlintCheck detekt
                EXIT_CODE=${'$'}?
            fi

            if [ ${'$'}EXIT_CODE -ne 0 ]; then
                echo "Pre-commit checks failed. Attempting to auto-format..."
                if [ -f "./gradlew.bat" ] && [ "${'$'}OSTYPE" = "win32" -o "${'$'}OSTYPE" = "msys" -o "${'$'}OSTYPE" = "cygwin" ]; then
                    cmd.exe /c gradlew.bat ktlintFormat
                else
                    ./gradlew ktlintFormat
                fi
                echo "Code has been auto-formatted. Please review and commit again."
                exit 1
            else
                echo "Pre-commit checks passed."
            fi
            """.trimIndent(),
        )
        preCommitFile.setExecutable(true)

        // Commit-msg hook
        val commitMsgFile = file("${project.rootDir}/.git/hooks/commit-msg")
        commitMsgFile.writeText(
            """
            #!/usr/bin/env sh

            commit_file=${'$'}1
            commit_msg=$(cat "${'$'}commit_file")

            pattern="^(feat|fix|docs|style|refactor|test|chore)(\([a-z0-9-]+\))?: .+"

            if ! echo "${'$'}commit_msg" | grep -E "${'$'}pattern" > /dev/null; then
                echo "Error: Commit message does not follow conventional format."
                echo "Should start with feat:, fix:, docs:, style:, refactor:, test: or chore:"
                echo "Example: feat: add new functionality"
                exit 1
            fi
            exit 0
            """.trimIndent(),
        )
        commitMsgFile.setExecutable(true)

        println("Git hooks installed successfully.")
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
