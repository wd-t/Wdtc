package org.wdt;

public class Version extends GetGamePath {
    private static String version;

    public Version(String version) {
        Version.version = version;
    }

    public Version(String version, String here) {
        super(here);
        Version.version = version;
    }

    public String getVersion() {
        return version;
    }

    public String getVersionLibPath() {
        return getGameVersionPath();
    }

    public String getVersionPath() {
        return getVersionLibPath() + version + "\\";
    }

    public String getVersionJson() {
        return getVersionPath() + version + ".json";
    }
    public String getVersionJar() {
        return getVersionPath() + version + ".jar";
    }
    public String getVersionLog4j2() {
        return getVersionPath() + "log4j2.xml";
    }
    public String getVersionNativesPath() {
        return getVersionPath() + "natives-windows-x86_64";
    }
}
