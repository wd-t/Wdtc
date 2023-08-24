package org.wdt.wdtc.game;


import org.wdt.platform.gson.JSONUtils;
import org.wdt.wdtc.launch.GamePath;

import java.io.File;
import java.io.IOException;

public class Version extends GamePath {
    protected final String VersionNumber;

    public Version(String VersionNumber) {
        this.VersionNumber = VersionNumber;
    }

    public Version(String VersionNumber, File here) {
        super(here);
        this.VersionNumber = VersionNumber;
    }

    public String getVersionNumber() {
        return VersionNumber;
    }


    public File getVersionPath() {
        return new File(getGameVersionsPath(), VersionNumber);
    }

    public File getVersionJson() {
        return new File(getVersionPath(), VersionNumber + ".json");
    }

    public File getVersionJar() {
        return new File(getVersionPath(), VersionNumber + ".jar");
    }

    public File getVersionLog4j2() {
        return new File(getVersionPath(), "log4j2.xml");
    }

    public File getVersionNativesPath() {
        return new File(getVersionPath(), "natives-windows-x86_64");
    }

    public File getGameAssetsListJson() throws IOException {
        return new File(getGameAssetsdir(), "indexes/" + getGameVersionJsonObject().getAssets() + ".json");
    }

    public File getGameOptionsFile() {
        return new File(getVersionPath(), "options.txt");
    }

    public File getGameModsPath() {
        return new File(getVersionPath(), "mods");
    }

    public File getGameLogDir() {
        return new File(getVersionPath(), "logs");
    }

    public void PutToVersionJson(GameVersionJsonObject o) {
        JSONUtils.ObjectToJsonFile(getVersionJson(), o);
    }

    public GameVersionJsonObject getGameVersionJsonObject() throws IOException {
        return JSONUtils.JsonFileToClass(getVersionJson(), GameVersionJsonObject.class);
    }

    public File getVersionConfigFile() {
        return new File(getVersionPath(), "config.json");
    }
}
