plugins {
    kotlin("multiplatform") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.0"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
    id("io.gitlab.arturbosch.detekt") version "1.23.6"
    id("org.jetbrains.dokka") version "1.9.20" // Per la documentazione
    id("org.danilopianini.git-sensitive-semantic-versioning") version "2.0.5" // Per versioning basato su git
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
    description = "Installa i git hooks (pre-commit e commit-msg) compatibili con Windows"
    group = "git hooks"
    doLast {
        // Pre-commit hook
        val preCommitFile = file("${project.rootDir}/.git/hooks/pre-commit")
        preCommitFile.parentFile.mkdirs()
        preCommitFile.writeText(
            """
            #!/usr/bin/env sh
            echo "Running ktlint and detekt checks..."

            # Rileva Windows e usa il comando appropriato
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

        // Commit-msg hook (come prima)
        val commitMsgFile = file("${project.rootDir}/.git/hooks/commit-msg")
        commitMsgFile.writeText(
            """
            #!/usr/bin/env sh

            commit_file=${'$'}1
            commit_msg=$(cat "${'$'}commit_file")

            pattern="^(feat|fix|docs|style|refactor|test|chore)(\([a-z0-9-]+\))?: .+"

            if ! echo "${'$'}commit_msg" | grep -E "${'$'}pattern" > /dev/null; then
                echo "Errore: Il messaggio di commit non segue il formato convenzionale."
                echo "Dovrebbe iniziare con feat:, fix:, docs:, style:, refactor:, test: o chore:"
                echo "Esempio: feat: aggiungi nuova funzionalità"
                exit 1
            fi
            exit 0
            """.trimIndent(),
        )
        commitMsgFile.setExecutable(true)

        println("Git hooks installati con successo.")
    }
}

// Task per formattare tutto il codice
tasks.register("formatCode") {
    group = "code quality"
    description = "Formatta tutti i file Kotlin del progetto"
    dependsOn("ktlintFormat")

    doLast {
        println("✅ Tutti i file Kotlin sono stati formattati!")
    }
}

// Task per controllare tutto il codice
tasks.register("checkCode") {
    group = "code quality"
    description = "Esegue tutti i controlli di qualità del codice"
    dependsOn("ktlintCheck", "detekt")

    doLast {
        println("✅ Tutti i controlli di qualità sono stati completati!")
    }
}

// Task per fix automatico completo
tasks.register("fixCode") {
    group = "code quality"
    description = "Corregge automaticamente tutti i problemi di stile risolvibili"
    dependsOn("ktlintFormat")

    doLast {
        println("✅ Codice formattato e problemi di stile risolti automaticamente!")
        println("💡 Esegui 'gradlew checkCode' per verificare eventuali problemi rimanenti")
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
