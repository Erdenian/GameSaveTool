plugins {
    kotlin("multiplatform") version "1.4.0-rc"
}

repositories {
    mavenCentral()
}

kotlin {
    mingwX64("mingw") {
        compilations["main"].enableEndorsedLibs = true
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }

    sourceSets {
        all {
            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
        }
    }
}
