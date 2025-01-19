object Constants {
    // https://fabricmc.net/develop/
    const val MINECRAFT_VERSION: String = "24w14potato"
    // build.4 has a conflict
    const val YARN_VERSION: String = "24w14potato+build.3"
    const val FABRIC_LOADER_VERSION: String = "0.15.8"
    const val FABRIC_API_VERSION: String = "0.96.14+24w14potato"

    // https://semver.org/
    const val MOD_VERSION: String = "0.5.8"
}

plugins {
    // Unlike most projects, we choose to pin the specific version of Loom.
    // This prevents a lot of issues where the build script can fail randomly because the Fabric Maven server
    // is not reachable for some reason, and it makes builds much more reproducible. Observation also shows that it
    // really helps to improve startup times on slow connections.
    id("fabric-loom") version "1.5.7"
}

base {
    archivesName = "sodium-fabric"

    group = "me.jellysquid.mods"
    version = "${Constants.MOD_VERSION}+mc${Constants.MINECRAFT_VERSION}"
}

loom {
    mixin {
        defaultRefmapName = "sodium.refmap.json"
    }

    accessWidenerPath = file("src/main/resources/sodium.accesswidener")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

sourceSets {
    val main = getByName("main")
    val api = create("api")
    val desktop = create("desktop")

    api.apply {
        java {
            compileClasspath += main.compileClasspath
        }
    }

    desktop.apply {
        java {
            srcDir("src/desktop/java")
        }
    }

    main.apply {
        java {
            compileClasspath += api.output
            runtimeClasspath += api.output
        }
    }
}

dependencies {
    minecraft(group = "com.mojang", name = "minecraft", version = Constants.MINECRAFT_VERSION)
    mappings(group = "net.fabricmc", name = "yarn", version = Constants.YARN_VERSION, classifier = "v2")
    modImplementation(group = "net.fabricmc", name = "fabric-loader", version = Constants.FABRIC_LOADER_VERSION)

    fun addEmbeddedFabricModule(name: String) {
        val module = fabricApi.module(name, Constants.FABRIC_API_VERSION)
        modImplementation(module)
        include(module)
    }

    // Fabric API modules
    addEmbeddedFabricModule("fabric-api-base")
    addEmbeddedFabricModule("fabric-block-view-api-v2")
    addEmbeddedFabricModule("fabric-rendering-fluids-v1")
    addEmbeddedFabricModule("fabric-rendering-data-attachment-v1")
    addEmbeddedFabricModule("fabric-resource-loader-v0")
}

tasks {
    getByName<JavaCompile>("compileDesktopJava") {
        sourceCompatibility = JavaVersion.VERSION_1_8.toString()
        targetCompatibility = JavaVersion.VERSION_1_8.toString()
    }

    jar {
        from("${rootProject.projectDir}/COPYING")
        from("${rootProject.projectDir}/COPYING.LESSER")

        val api = sourceSets.getByName("api")
        from(api.output.classesDirs)
        from(api.output.resourcesDir)

        val desktop = sourceSets.getByName("desktop")
        from(desktop.output.classesDirs)
        from(desktop.output.resourcesDir)

        manifest.attributes["Main-Class"] = "net.caffeinemc.mods.sodium.desktop.LaunchWarn"
    }

    processResources {
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand(mapOf("version" to project.version))
        }
    }
}

// ensure that the encoding is set to UTF-8, no matter what the system default is
// this fixes some edge cases with special characters not displaying correctly
// see http://yodaconditions.net/blog/fix-for-java-file-encoding-problems-with-gradle.html
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
