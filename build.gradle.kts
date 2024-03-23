plugins {
  `java-library`
  id("io.papermc.paperweight.userdev") version "1.5.5"
  id("xyz.jpenilla.run-paper") version "2.2.0"
  id("com.github.johnrengelman.shadow") version "7.1.2"
  id("io.freefair.lombok") version "6.1.0"
  id("maven-publish")
}

group = "org.motion"
version = "1.0"
description = "Motion Core - 1.20"

repositories {
  mavenLocal()
  mavenCentral()
  maven("https://mvn.lumine.io/repository/maven-public/")
  maven("https://libraries.minecraft.net/")
  maven("https://plugins.gradle.org/m2")
  maven("https://repo.aikar.co/content/groups/aikar/")
  maven("https://jitpack.io")
  maven("https://maven.enginehub.org/repo/")

  maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/") {
    name = "sonatype-oss-snapshots1"
  }
}

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

dependencies {
  paperweight.paperDevBundle("1.20.1-R0.1-SNAPSHOT")

  implementation("net.kyori:adventure-api:4.14.0")
  implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")
  implementation("net.dv8tion:JDA:5.0.0-alpha.9")
  implementation("co.aikar:taskchain-bukkit:3.7.2")

  implementation("net.kyori:adventure-platform-bukkit:4.1.2")
  implementation("net.kyori:adventure-text-serializer-legacy:4.11.0")

  compileOnly("org.projectlombok:lombok:1.18.24")
  annotationProcessor("org.projectlombok:lombok:1.18.24")
  testCompileOnly("org.projectlombok:lombok:1.18.24")
  testAnnotationProcessor("org.projectlombok:lombok:1.18.24")

}

tasks {
  assemble {
    dependsOn(reobfJar)
  }

  compileJava {
    options.encoding = Charsets.UTF_8.name()

    options.release.set(17)
  }

  javadoc {
    options.encoding = Charsets.UTF_8.name()
  }

  processResources {
    filteringCharset = Charsets.UTF_8.name()
    val props = mapOf(
      "name" to project.name,
      "version" to project.version,
      "description" to project.description,
      "apiVersion" to "1.20"
    )
    inputs.properties(props)
    filesMatching("plugin.yml") {
      expand(props)
    }
  }

  reobfJar {
    outputJar.set(layout.buildDirectory.file("libs/MotionCore.jar"))
  }

  shadowJar {
    relocate("co.aikar.commands", "org.motion.acf")
    relocate("co.aikar.locales", "org.motion.locales")
  }

}
