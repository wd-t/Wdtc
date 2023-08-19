package org.wdt.wdtc.download.fabric;


import org.apache.log4j.Logger;
import org.wdt.platform.gson.JSONObject;
import org.wdt.platform.gson.JSONUtils;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.download.infterface.DownloadInfo;
import org.wdt.wdtc.download.infterface.InstallTask;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.File;
import java.io.IOException;

public class FabricDonwloadInfo implements DownloadInfo {
    private static final Logger logmaker = WdtcLogger.getLogger(FabricDonwloadInfo.class);
    protected final String FabricVersionNumber;
    protected final Launcher launcher;
    private FabricAPIDownloadTask APIDownloadTask;


    public FabricDonwloadInfo(Launcher launcher, String FabricVersionNumber) {
        this.FabricVersionNumber = FabricVersionNumber;
        this.launcher = launcher;
    }

    public String getFabricVersionNumber() {
        return FabricVersionNumber;
    }


    public String getFabricVersionFileUrl() {
        return Launcher.getOfficialDownloadSource().getFabricMetaUrl() + "v2/versions/loader/%s/%s/profile/json";
    }


    public void DownloadProfileZip() {
        DownloadTask.StartDownloadTask(getProfileZipUrl(), getProfileZipFile());
    }

    public String getProfileZipFile() {
        return String.format(FilePath.getWdtcCache() + "/%s-%s-frofile-zip.zip", launcher.getVersion(), getFabricVersionNumber());
    }

    public String getProfileZipUrl() {
        return String.format(Launcher.getOfficialDownloadSource().getFabricMetaUrl() + "v2/versions/loader/%s/%s/profile/zip", launcher.getVersion(), getFabricVersionNumber());
    }

    public String FromFabricLoaderFolder() {
        return String.format("fabric-loader-%s-%s", getFabricVersionNumber(), launcher.getVersion());
    }

    public File getFabricJar() {
        return new File(FilePath.getWdtcCache() + "/" + FromFabricLoaderFolder() + "/" + FromFabricLoaderFolder() + ".jar");
    }

    public File getFabricVersionJson() {
        return new File(String.format(FilePath.getWdtcCache() + "/%s-fabric-%s.json", launcher.getVersion(), FabricVersionNumber));
    }

    public JSONObject getFabricVersionJsonObject() throws IOException {
        return JSONUtils.getJSONObject(getFabricVersionJson());
    }



    public FabricAPIDownloadTask getAPIDownloadTask() {
        return APIDownloadTask;
    }

    public void setAPIDownloadTask(FabricAPIDownloadTask APIDownloadTask) {
        this.APIDownloadTask = APIDownloadTask;
    }

    public boolean getAPIDownloadTaskNoNull() {
        return APIDownloadTask != null;
    }

    @Override
    public String getModVersion() {
        return FabricVersionNumber;
    }

    @Override
    public InstallTask getModInstallTask() {
        return new FabricInstallTask(launcher, FabricVersionNumber);
    }
}