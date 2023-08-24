package org.wdt.wdtc.platform;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;
import org.wdt.platform.gson.JSONObject;
import org.wdt.platform.gson.JSONUtils;
import org.wdt.utils.FileUtils;
import org.wdt.utils.IOUtils;
import org.wdt.wdtc.download.FileUrl;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.utils.PlatformUtils;

import java.io.File;
import java.io.IOException;

import static java.util.Objects.requireNonNull;

public class AboutSetting {
    private static final Logger logmaker = Logger.getLogger(AboutSetting.class);

    public static File GetSettingFile() {
        return new File(FilePath.getWdtcConfig(), "setting/setting.json");
    }

    public static void GenerateSettingFile() throws IOException {
        String readme = IOUtils.toString(requireNonNull(AboutSetting.class.getResourceAsStream("/readme.txt")));
        File writeReadme = new File(FilePath.getWdtcConfig(), "readme.txt");
        FileUtils.writeStringToFile(writeReadme, readme);
        if (PlatformUtils.FileExistenceAndSize(FilePath.getUserListFile())) {
            JSONUtils.ObjectToJsonFile(FilePath.getUserListFile(), new JsonObject());
        }
        if (PlatformUtils.FileExistenceAndSize(GetSettingFile())) {
            JSONUtils.ObjectToJsonFile(GetSettingFile(), new Setting());
        }
    }

    public static JSONObject SettingObject() {
        try {
            return JSONUtils.getJSONObject(GetSettingFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Setting getSetting() {
        try {
            return JSONUtils.JsonFileToClass(GetSettingFile(), Setting.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void putSettingToFile(Setting setting) {
        JSONUtils.ObjectToJsonFile(GetSettingFile(), setting);
    }

    public static class Setting {
        private FileUrl.DownloadSourceList DownloadSource = FileUrl.DownloadSourceList.OFFICIAL;
        private boolean console = false;
        private boolean LlvmpipeLoader = false;
        private String DefaultGamePath = System.getProperty("user.dir");
        private JsonArray JavaPath = new JsonArray();
        private boolean ChineseLanguage = true;
        private double WindowsWidth = 616.0;
        private double WindowsHeight = 489.0;
        private String PreferredVersion;

        public String getPreferredVersion() {
            return PreferredVersion;
        }

        public void setPreferredVersion(String preferredVersion) {
            PreferredVersion = preferredVersion;
        }

        public double getWindowsWidth() {
            return WindowsWidth;
        }

        public void setWindowsWidth(double windowsWidth) {
            WindowsWidth = windowsWidth;
        }

        public double getWindowsHeight() {
            return WindowsHeight;
        }

        public void setWindowsHeight(double windowsHeight) {
            WindowsHeight = windowsHeight;
        }

        public boolean isConsole() {
            return console;
        }

        public void setConsole(boolean console) {
            this.console = console;
        }

        public boolean isLlvmpipeLoader() {
            return LlvmpipeLoader;
        }

        public void setLlvmpipeLoader(boolean llvmpipeLoader) {
            LlvmpipeLoader = llvmpipeLoader;
        }

        public File getDefaultGamePath() {
            return new File(DefaultGamePath);
        }

        public void setDefaultGamePath(File defaultGamePath) {
            DefaultGamePath = FileUtils.getCanonicalPath(defaultGamePath);
        }

        public JsonArray getJavaPath() {
            return JavaPath;
        }

        public void setJavaPath(JsonArray javaPath) {
            JavaPath = javaPath;
        }

        public boolean isChineseLanguage() {
            return ChineseLanguage;
        }

        public void setChineseLanguage(boolean chineseLanguage) {
            ChineseLanguage = chineseLanguage;
        }

        public FileUrl.DownloadSourceList getDownloadSource() {
            return DownloadSource;
        }

        public void setDownloadSource(FileUrl.DownloadSourceList downloadSource) {
            DownloadSource = downloadSource;
        }

        @Override
        public String toString() {
            return "Setting{" +
                    "DownloadSource=" + DownloadSource +
                    ", console=" + console +
                    ", LlvmpipeLoader=" + LlvmpipeLoader +
                    ", DefaultGamePath='" + DefaultGamePath + '\'' +
                    ", JavaPath=" + JavaPath +
                    ", ChineseLanguage=" + ChineseLanguage +
                    ", WindowsWidth=" + WindowsWidth +
                    ", WindowsHeight=" + WindowsHeight +
                    ", PreferredVersion='" + PreferredVersion + '\'' +
                    '}';
        }
    }
}