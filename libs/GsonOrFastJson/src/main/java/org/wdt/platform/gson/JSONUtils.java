package org.wdt.platform.gson;

import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class JSONUtils extends JSON {
    public static JSONObject getJSONObject(File file) throws IOException {
        return new JSONObject(FileUtils.readFileToString(file, "UTF-8"));
    }

    public static JSONObject getJSONObject(String filepath) throws IOException {
        return getJSONObject(new File(filepath));
    }

    public static JsonObject getJsonObject(File JsonFile) throws IOException {
        return parseJsonObject(FileUtils.readFileToString(JsonFile, "UTF-8"));
    }

    public static JsonObject getJsonObject(String JsonFilePath) throws IOException {
        return getJsonObject(new File(JsonFilePath));
    }

    public static <T> T JsonFileToClass(File JsonFile, Class<T> clazz) throws IOException {
        return JSONObject.parseObject(FileUtils.readFileToString(JsonFile, "UTF-8"), clazz);
    }

    public static <T> T JsonFileToClass(String JsonFilePath, Class<T> clazz) throws IOException {
        return JsonFileToClass(new File(JsonFilePath), clazz);
    }

    public static void ObjectToJsonFile(File JsonFile, Object object) {
        try {
            FileUtils.writeStringToFile(JsonFile, JSONObject.toJSONString(object), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void ObjectToJsonFile(String JsonFilePath, Object object) {
        ObjectToJsonFile(new File(JsonFilePath), object);
    }
}
