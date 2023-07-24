package org.wdt.platform.gson;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class JSONUtils {
    public static JSONObject getJSONObject(File file) throws IOException {
        return new JSONObject(FileUtils.readFileToString(file, "UTF-8"));
    }

    public static JSONObject getJSONObject(String filepath) throws IOException {
        return getJSONObject(new File(filepath));
    }

    public static void PutJSONObject(File file, JSONObject jsonObject) {
        try {
            FileUtils.writeStringToFile(file, jsonObject.toString(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}