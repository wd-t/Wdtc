subprojects {
  repositories {
    maven { url = uri("https://jitpack.io") }
    mavenLocal()
    mavenCentral()
  }
  apply {
    plugin("application")
  }
}

val versionNumber = "0.0.1.20.2"
group = "org.wdt.wdtc"
version = "$versionNumber-kotlin"
