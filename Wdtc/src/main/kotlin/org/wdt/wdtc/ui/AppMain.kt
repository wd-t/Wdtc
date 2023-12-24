package org.wdt.wdtc.ui

import javafx.application.Application
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.image.Image
import javafx.stage.Stage
import org.wdt.utils.io.touch
import org.wdt.wdtc.core.manger.isDebug
import org.wdt.wdtc.core.manger.putSettingToFile
import org.wdt.wdtc.core.manger.setting
import org.wdt.wdtc.core.utils.getExceptionMessage
import org.wdt.wdtc.core.utils.isOnline
import org.wdt.wdtc.core.utils.logmaker
import org.wdt.wdtc.core.utils.stopProcess
import org.wdt.wdtc.ui.window.*
import java.io.IOException

class AppMain : Application() {
  override fun start(mainStage: Stage) {
    try {
      val size = mainStage.getSizeManger()
      mainStage.title = if (isOnline) windowsTitle else getWindowsTitle("无网络")
      if (!isOnline) logmaker.warn("当前无网络连接,下载功能无法正常使用!")
      mainStage.minWidth = windowsWidht
      mainStage.minHeight = windowsHeight
      size.setWindwosSize()
      mainStage.icons.add(Image("assets/icon/ico.jpg"))
      mainStage.isResizable = isDebug
      val win = HomeWindow()
      win.setHome(mainStage)
      mainStage.show()
      logmaker.info("Window Show")
      mainStage.onCloseRequest = EventHandler {
        logmaker.info(size)
        try {
          setting.windowsWidth = mainStage.width
          setting.windowsHeight = mainStage.height
          stopProcess.touch()
          setting.putSettingToFile()
        } catch (e: IOException) {
          logmaker.error(e.getExceptionMessage())
        }
        Platform.exit()
        logmaker.info("======= Wdtc Stop ========")
      }
    } catch (e: Exception) {
      setErrorWin(e)
    }
  }
//  @JvmStatic
//  fun main(args: Array<String>) {
//    WdtcMain.main(args)
//    launch(AppMain::class.java, *args)
//  }
}
