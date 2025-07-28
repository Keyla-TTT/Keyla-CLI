plugins {
    kotlin("jvm") version "2.0.0"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
    id("io.gitlab.arturbosch.detekt") version "1.23.6"
    id("org.jetbrains.dokka") version "1.9.20" // Per la documentazione
    id("org.danilopianini.git-sensitive-semantic-versioning") version "2.0.5" // Per versioning basato su git
}

tasks.register("installGitHooks") {
    description = "Installa i git hooks (pre-commit e commit-msg) compatibili con Windows"
    group = "git hooks"
    doLast {
        // Pre-commit hook
        val preCommitFile = file("${project.rootDir}/.git/hooks/pre-commit")
        preCommitFile.parentFile.mkdirs()
        preCommitFile.writeText("""
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
        """.trimIndent())
        preCommitFile.setExecutable(true)

        // Commit-msg hook (come prima)
        val commitMsgFile = file("${project.rootDir}/.git/hooks/commit-msg")
        commitMsgFile.writeText("""
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
        """.trimIndent())
        commitMsgFile.setExecutable(true)

        println("Git hooks installati con successo.")
    }
}

group = "org.example"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.9.20")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
