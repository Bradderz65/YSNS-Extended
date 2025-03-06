import com.matthewprenger.cursegradle.CurseArtifact
import com.matthewprenger.cursegradle.CurseProject
import com.matthewprenger.cursegradle.CurseRelation
import com.matthewprenger.cursegradle.Options
import gg.essential.gradle.util.noServerRunConfigs

plugins {
    id(egt.plugins.multiversion.get().pluginId)
    id(egt.plugins.defaults.get().pluginId)
    alias(libs.plugins.shadow)
    alias(libs.plugins.blossom)
    alias(libs.plugins.minotaur)
    alias(libs.plugins.cursegradle)
}

val archiveBase: String by project
val mod_name: String by project
val mod_version: String by project
val mod_id: String by project

val necronomicon_version: String by project
val fabric_api_version: String by project

preprocess {
    vars.put("MODERN", if (project.platform.mcMinor >= 16) 1 else 0)
}

blossom {
    replaceToken("@NAME@", mod_name)
    replaceToken("@ID@", mod_id)
    replaceToken("@VER@", mod_version)
}

version = mod_version
group = "elocindev.ysns"

base {
    archivesName.set("$archiveBase-${platform.loaderStr}-${getMcVersionStr()}")
}

loom.noServerRunConfigs()

loom {
    mixin {
        useLegacyMixinAp = true
    }
    mixin.defaultRefmapName.set("${mod_id}.refmap.json")
}

repositories {
    maven("https://maven.terraformersmc.com/releases/")
    maven("https://repo.essential.gg/repository/maven-public/")
    maven("https://maven.dediamondpro.dev/releases")
    maven("https://maven.isxander.dev/releases")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
    
    maven("https://api.modrinth.com/maven")

    maven("https://maven.fabricmc.net")
    
    mavenCentral()
}

val shade: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

dependencies {
    modImplementation("maven.modrinth:necronomicon:${necronomicon_version}-fabric")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.92.2+1.20.1") 
    implementation("org.joml:joml:1.10.5")
}

tasks.processResources {
    inputs.property("id", mod_id)
    inputs.property("name", mod_name)
    val java = 17
    
    val compatLevel = "JAVA_${java}"

    inputs.property("java", java)
    inputs.property("java_level", compatLevel)
    inputs.property("version", mod_version)
    inputs.property("mcVersionStr", project.platform.mcVersionStr)
    filesMatching(listOf("mcmod.info", "mods.toml", "fabric.mod.json")) {
        expand(
            mapOf(
                "id" to mod_id,
                "name" to mod_name,
                "java" to java,
                "java_level" to compatLevel,
                "version" to mod_version,
                "mcVersionStr" to getInternalMcVersionStr()
            )
        )
    }
}

tasks {
    withType<Jar> {
        exclude("mcmod.info", "mods.toml", "neoforge.mods.toml", "pack.mcmeta")
        from(rootProject.file("LICENSE"))
        from(rootProject.file("LICENSE.LESSER"))
    }

    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        archiveClassifier.set("dev")
        configurations = listOf(shade)
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    remapJar {
        input.set(shadowJar.get().archiveFile)
        archiveClassifier.set("")
        finalizedBy("copyJar")
    }

    jar {
        dependsOn(shadowJar)
        archiveClassifier.set("")
        enabled = false
    }

    register<Copy>("copyJar") {
        File("${project.rootDir}/jars").mkdir()
        from(remapJar.get().archiveFile)
        into("${project.rootDir}/jars")
    }

    clean { delete("${project.rootDir}/jars") }

    project.modrinth {
        token.set(System.getenv("MR_TOKEN"))
        projectId.set("EjXcpmEA")
        versionNumber.set(mod_version)
        versionName.set("You Shall Not Spawn $mod_version")
        uploadFile.set(remapJar.get().archiveFile as Any)
        gameVersions.addAll(getMcVersionList())
        loaders.add("fabric")
        loaders.add("quilt")
        changelog.set(file("../../changelog.md").readText())
        dependencies {
            required.project("necronomicon")
        }
    }

    project.curseforge {
        project(closureOf<CurseProject> {
            apiKey = System.getenv("CF_TOKEN")
            id = "628744"
            changelog = file("../../changelog.md")
            changelogType = "markdown"
            relations(closureOf<CurseRelation> {
                requiredDependency("necronomicon")
            })
            gameVersionStrings.addAll(getMcVersionList())
            addGameVersion("Fabric")
            addGameVersion("Quilt")
            releaseType = "release"
            mainArtifact(remapJar.get().archiveFile, closureOf<CurseArtifact> {
                displayName = "[${getMcVersionStr()}-${platform.loaderStr}] You Shall Not Spawn v$mod_version"
            })
        })
        options(closureOf<Options> {
            javaVersionAutoDetect = false
            javaIntegration = false
            forgeGradleIntegration = false
        })
    }

    register("publish") {
        dependsOn(curseforge)
        dependsOn(modrinth)
    }
}

fun getMcVersionStr(): String {
    return when (project.platform.mcVersionStr) {
        else -> {
            val dots = project.platform.mcVersionStr.count { it == '.' }
            if (dots == 1) "${project.platform.mcVersionStr}.x"
            else "${project.platform.mcVersionStr.substringBeforeLast(".")}.x"
        }
    }
}

fun getInternalMcVersionStr(): String {
    return when (project.platform.mcVersionStr) {
        else -> {
            val dots = project.platform.mcVersionStr.count { it == '.' }
            if (dots == 1) "${project.platform.mcVersionStr}.x"
            else "${project.platform.mcVersionStr.substringBeforeLast(".")}.x"
        }
    }
}

fun getMcVersionList(): List<String> {
    return when (project.platform.mcVersionStr) {
        "1.21" -> listOf("1.21")
        "1.20.1" -> listOf("1.20", "1.20.1")
        "1.19.2" -> listOf("1.19.2")
        else -> error("Unknown version")
    }
}