package org.wdt.wdtc.auth.Yggdrasil;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;
import org.wdt.platform.gson.JSONObject;
import org.wdt.platform.gson.JSONUtils;
import org.wdt.utils.IOUtils;
import org.wdt.wdtc.auth.Accounts;
import org.wdt.wdtc.auth.AccountsInterface;
import org.wdt.wdtc.auth.User;
import org.wdt.wdtc.auth.skin.SkinUtils;
import org.wdt.wdtc.download.UrlManger;
import org.wdt.wdtc.game.FileManger;
import org.wdt.wdtc.utils.PlatformUtils;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

public class YggdrasilAccounts implements AccountsInterface {
    private static final Logger logmaker = WdtcLogger.getLogger(YggdrasilAccounts.class);
    private final String url;
    private final String username;
    private final String password;

    public YggdrasilAccounts(String url, String username, String password) {
        this.password = password;
        this.username = username;
        this.url = url;
    }

    public String sendPostWithJson() throws IOException {
        URL requestUrl = new URL(url + "/api/yggdrasil/authserver/authenticate");
        String jsonStr = "{" +
                "\"username\":\"" + username + "\"," +
                "\"password\":\"" + password + "\"," +
                "\"requestUser\":true," +
                "\"agent\":{" +
                "\"name\":\"Minecraft\"," +
                "\"version\":1" +
                "}" +
                "}";
        URLConnection conn = requestUrl.openConnection();
        conn.setRequestProperty("content-type", "application/json");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        PrintWriter out = new PrintWriter(conn.getOutputStream());
        out.print(jsonStr);
        out.flush();
        return IOUtils.toString(conn.getInputStream());
    }

    public String getUrl() {
        return url;
    }


    public String getUsername() {
        return username;
    }

    public UserInformation getUserInformation() throws IOException {
        return JSONObject.parseObject(sendPostWithJson(), UserInformation.class);
    }

    public YggdrasilTextures getYggdrasilTextures() {
        return new YggdrasilTextures(this);
    }

    @Override
    public User getUser() throws IOException {
        UserInformation UserInfo = getUserInformation();
        YggdrasilTextures textures = getYggdrasilTextures();
        User user = new User();
        user.setType(Accounts.AccountsType.Yggdrasil);
        JSONObject selectedProfile = new JSONObject(UserInfo.getSelectedProfile());
        user.setUserName(selectedProfile.getString("name"));
        user.setUuid(selectedProfile.getString("id"));
        user.setAccessToken(UserInfo.getAccessToken());
        user.setAPI(IOUtils.toString(UrlManger.getLittleskinApi()));
        user.setAPIBase64(PlatformUtils.StringToBase64(user.getAPI()));
        SkinUtils utils = textures.getUtils();
        user.setHeadFile(utils.writeSkinHead());
        JSONUtils.ObjectToJsonFile(FileManger.getUsersJson(), user);
        logmaker.info(user);
        return user;
    }

    public static class UserInformation {
        public String accessToken;
        public String clientToken;
        public JsonArray availableProfiles;
        public JsonObject user;
        public JsonObject selectedProfile;

        public String getAccessToken() {
            return accessToken;
        }

        public String getClientToken() {
            return clientToken;
        }

        public JsonArray getAvailableProfiles() {
            return availableProfiles;
        }

        public JsonObject getUser() {
            return user;
        }

        public JsonObject getSelectedProfile() {
            return selectedProfile;
        }


        @Override
        public String toString() {
            return "UserInformation{" +
                    "accessToken='" + accessToken + '\'' +
                    ", clientToken='" + clientToken + '\'' +
                    ", availableProfiles=" + availableProfiles +
                    ", user=" + user +
                    ", selectedProfile=" + selectedProfile +
                    '}';
        }
    }

}
