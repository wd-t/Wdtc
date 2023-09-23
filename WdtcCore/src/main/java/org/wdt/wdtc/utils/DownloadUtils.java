package org.wdt.wdtc.utils;

import org.apache.log4j.Logger;
import org.wdt.utils.io.FileUtils;
import org.wdt.wdtc.manger.SettingManger;

import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class DownloadUtils {
    private static final Logger logmaker = WdtcLogger.getLogger(DownloadUtils.class);
    private final File DownloadFile;
    private final URL SrcousFileUrl;

    public DownloadUtils(File downloadFile, URL srcousFileUrl) {
        DownloadFile = downloadFile;
        SrcousFileUrl = srcousFileUrl;
    }

    public void DownloadFile() throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) SrcousFileUrl.openConnection();
        FileUtils.touch(DownloadFile);
        OutputStream DownloadFileOutput = FileUtils.newOutputStream(DownloadFile);
        InputStream UrlFileInput = connection.getInputStream();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
//        double InitialFileSize = 0;
        double Donwloaded;
        Thread.currentThread().setName(DownloadFile.getName());
        byte[] data = new byte[1024];
        while ((Donwloaded = UrlFileInput.read(data, 0, 1024)) >= 0 && SettingManger.getSetting().isDownloadProcess()) {
//            InitialFileSize += Donwloaded;
            DownloadFileOutput.write(data, 0, (int) Donwloaded);
        }
        DownloadFileOutput.close();
        UrlFileInput.close();
    }

    public void ManyTimesToTryDownload(int times) {
        IOException exception = null;
        for (int i = 0; i < times; i++) {
            try {
                DownloadFile();
                return;
            } catch (IOException e) {
                exception = e;
            }
        }
        if (exception != null) {
            logmaker.warn("* Thread " + Thread.currentThread().getName() + " Error,", exception);
        }
    }
}
