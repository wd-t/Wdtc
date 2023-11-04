package org.wdt.wdtc.ui.window.user;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import org.apache.log4j.Logger;
import org.wdt.wdtc.core.auth.UsersList;
import org.wdt.wdtc.core.auth.yggdrasil.YggdrasilAccounts;
import org.wdt.wdtc.core.auth.yggdrasil.YggdrasilTextures;
import org.wdt.wdtc.core.manger.URLManger;
import org.wdt.wdtc.core.utils.WdtcLogger;
import org.wdt.wdtc.ui.window.ExceptionWindow;

import java.io.IOException;


public class LittleskinWindow {
    private static final Logger logmaker = WdtcLogger.getLogger(LittleskinWindow.class);

    public static void setLittleskinWin(Pane pane) {
        pane.getChildren().clear();
        Label littleskinTitle = new Label("Littleskin外置登录");
        littleskinTitle.setLayoutX(250.0);
        littleskinTitle.setLayoutY(69.0);
        Label username = new Label("用户名");
        username.setLayoutX(175.0);
        username.setLayoutY(107.0);
        TextField Inputusername = new TextField();
        Inputusername.setLayoutX(220.0);
        Inputusername.setLayoutY(103.0);
        Label powerWordTip = new Label("密码:");
        powerWordTip.setLayoutX(179.0);
        powerWordTip.setLayoutY(135.0);
        TextField inputpowerword = new TextField();
        inputpowerword.setLayoutX(221.0);
        inputpowerword.setLayoutY(131.0);
        Label label = new Label();
        label.setLayoutX(220.0);
        label.setLayoutY(185.0);
        label.setPrefHeight(15.0);
        label.setPrefWidth(110.0);
        Button ok = new Button("登录");
        ok.setLayoutX(267.0);
        ok.setLayoutY(219.0);
        pane.getChildren().addAll(littleskinTitle, username, Inputusername, powerWordTip, inputpowerword, label, ok);
        ok.setOnAction(event -> {
            String UserName = Inputusername.getText();
            String PowerWord = inputpowerword.getText();
            if (UserName.isEmpty() && PowerWord.isEmpty()) {
                label.setText("请输入用户名、密码");
                logmaker.warn("用户名、密码为空");
            } else {
                try {
                    YggdrasilAccounts yggdrasilAccounts = new YggdrasilAccounts(URLManger.getLittleskinUrl(),
                            UserName, PowerWord);
                    UsersList.addUser(yggdrasilAccounts.getUser());
                    try {
                        YggdrasilTextures yggdrasilTextures = yggdrasilAccounts.getYggdrasilTextures();
                        yggdrasilTextures.DownloadUserSkin();
                        logmaker.info("Littleskin用户:" + UserName + "登陆成功!");
                        UserListPane.setUserList(pane);
                    } catch (IOException e) {
                        ExceptionWindow.setErrorWin(e);
                    }
                } catch (IOException e) {
                    label.setText("用户名或密码错误");
                    logmaker.warn("用户名或密码错误", e);
                }
            }
        });

    }
}