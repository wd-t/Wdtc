package org.wdt.wdtc.core.launch

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.wdt.wdtc.core.utils.logmaker
import java.io.InputStream
import java.nio.charset.Charset
import java.util.regex.Pattern
import kotlin.system.measureTimeMillis

class LaunchProcess(
	private val process: ProcessBuilder,
	var printInfo: (String) -> Unit
) {
	private val builder = StringBuilder()
	suspend fun startLaunchGame() {
		val runtime = withContext(Dispatchers.IO) {
			process.start().run {
				measureTimeMillis {
					launch(CoroutineName("Read info inputStream")) { inputStream.getRunInfo() }
					launch(CoroutineName("Read error inputStream")) { errorStream.getRunInfo() }
				}
			}
		}
		logmaker.info("Game over. Game run time: $runtime ms")
	}
	
	private fun InputStream.getRunInfo() {
		val thread = Thread.currentThread()
		val reader = reader(Charset.forName("GBK")).buffered()
		var line: String
		while (reader.readLine().also { line = it } != null) {
			if (thread.isInterrupted) {
				launchErrorTask()
				return
			} else {
				line.let {
					if (Pattern.compile("FATAL").matcher(it).find()) {
						println(it)
						builder.append(it)
						thread.interrupt()
					} else {
						println(it)
						builder.append(it)
					}
				}
			}
		}
	}
	
	private fun launchErrorTask() {
		printInfo("启动失败:\n${builder}")
	}
}
