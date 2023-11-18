package org.wdt.wdtc.core.manger;


import lombok.Getter;
import org.wdt.utils.io.FileUtils;
import org.wdt.wdtc.core.download.game.DownloadVersionGameFile;
import org.wdt.wdtc.core.game.GameVersionJsonObject;
import org.wdt.wdtc.core.utils.gson.JSONUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

@Getter
public class GameFileManger extends GameDirectoryManger {
  protected final String VersionNumber;

  public GameFileManger(String VersionNumber) {
    this.VersionNumber = VersionNumber;
  }

  public GameFileManger(String VersionNumber, File here) {
    super(here);
    this.VersionNumber = VersionNumber;
  }

  public static void downloadVersionManifestJsonFileTask() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.add(Calendar.DATE, -7);
    try {
      if (FileUtils.isFileNotExists(FileManger.getVersionManifestFile()) ||
          FileUtils.isFileOlder(FileManger.getVersionManifestFile(), calendar.getTime())) {
        DownloadVersionGameFile.DownloadVersionManifestJsonFile();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


  public File getVersionPath() {
    return new File(getGameVersionsDirectory(), VersionNumber);
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
    return new File(getGameAssetsDirectory(), "indexes/" + getGameVersionJsonObject().getAssets() + ".json");
  }

  public File getGameOptionsFile() {
    return new File(getVersionPath(), "assets/options.txt");
  }

  public File getGameModsPath() {
    return new File(getVersionPath(), "mods");
  }

  public File getGameLogDir() {
    return new File(getVersionPath(), "logs");
  }

  public void putToVersionJson(GameVersionJsonObject o) {
    JSONUtils.writeObjectToFile(getVersionJson(), o);
  }

  public GameVersionJsonObject getGameVersionJsonObject() throws IOException {
    if (FileUtils.isFileNotExists(getVersionJson())) {
      throw new FileNotFoundException(getVersionJson() + " not exists");
    }
    return JSONUtils.readFileToClass(getVersionJson(), GameVersionJsonObject.class);
  }

  public File getLaucnherProfiles() {
    return new File(getGameDirectory(), "Launcher_profiles.json");
  }

  public File getVersionConfigFile() {
    return new File(getVersionPath(), "config.json");
  }
}
