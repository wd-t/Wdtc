package org.wdt.wdtc.ui

import javafx.application.Application
import javafx.application.Platform
import javafx.scene.image.Image
import javafx.stage.Stage
import kotlinx.coroutines.launch
import org.wdt.wdtc.core.manger.changeSettingToFile
import org.wdt.wdtc.core.manger.currentSetting
import org.wdt.wdtc.core.manger.isDebug
import org.wdt.wdtc.core.utils.info
import org.wdt.wdtc.core.utils.isOnline
import org.wdt.wdtc.core.utils.logmaker
import org.wdt.wdtc.ui.window.*
import java.io.IOException

class AppMain : Application() {
	override fun start(mainStage: Stage) {
		if (!isOnline) logmaker.warning("当前无网络连接,下载功能无法正常使用!")
		mainStage.apply {
			title = if (isOnline) windowsTitle else getWindowsTitle("无网络")
			minWidth = windowsWidht
			minHeight = windowsHeight
			val size = getSizeManger().also {
				it.setWindwosSize()
			}
			icons.add(Image("assets/icon/ico.jpg"))
			isResizable = isDebug
			setOnCloseRequest {
				logmaker.info(size)
				try {
					currentSetting.changeSettingToFile {
						windowsWidth = width
						windowsHeight = height
					}
				} catch (e: IOException) {
					setErrorWin(e)
				}
				Platform.exit()
			}
		}.also {
			javafxScope.launch {
				HomeWindow().run { setHome(it) }
				it.show()
				logmaker.info("Window Show")
			}
		}
	}
}
