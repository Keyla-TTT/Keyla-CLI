rootProject.name = "CLI-Kotlin"

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
    id("org.danilopianini.gradle-pre-commit-git-hooks") version "2.0.27"
}

//gitHooks {
//    commitMsg {
//        conventionalCommits { } // Only feat and fix
//    }
//    createHooks()
//}