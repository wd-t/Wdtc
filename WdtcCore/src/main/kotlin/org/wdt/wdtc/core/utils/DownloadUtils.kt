package org.wdt.wdtc.core.utils

import org.wdt.utils.io.isFileExists
import org.wdt.utils.io.newOutputStream
import org.wdt.utils.io.toFile
import org.wdt.utils.io.touch
import org.wdt.wdtc.core.manger.FileManger
import org.wdt.wdtc.core.utils.URLUtils.newInputStream
import org.wdt.wdtc.core.utils.URLUtils.toURL
import org.wdt.wdtc.core.utils.WdtcLogger.getWdtcLogger
import java.io.File
import java.io.IOException
import java.net.URL

class DownloadUtils(private val downloadFile: File, private val srcousFileURL: URL) {
  fun manyTimesToTryDownload(times: Int) {
    var exception: IOException? = null
    for (i in 0 until times) {
      exception = try {
        startDownloadFile()
        return
      } catch (e: IOException) {
        e
      }
    }
    if (exception != null) {
      logmaker.warn("Thread ${Thread.currentThread().name} Error,", exception)
    }
  }

  @Throws(IOException::class)
  fun startDownloadFile() {
    downloadFile.touch()
    val downloadFileOutput = downloadFile.newOutputStream()
    val urlFileInput = srcousFileURL.newInputStream()
    var donwloaded: Double
    Thread.currentThread().setName(downloadFile.getName())
    val data = ByteArray(1024)
    while (urlFileInput.read(data, 0, 1024).also { donwloaded = it.toDouble() } >= 0) {
      if (isDownloadProcess) {
        logmaker.debug(Thread.currentThread().name + " Stop")
        downloadFileOutput.close()
        urlFileInput.close()
        return
      }
      downloadFileOutput.write(data, 0, donwloaded.toInt())
    }
    downloadFileOutput.close()
    urlFileInput.close()
  }

  companion object {
    @JvmField
    val StopProcess = File(FileManger.wdtcCache, "process")
    private val logmaker = DownloadUtils::class.java.getWdtcLogger()

    @JvmStatic
    @get:Throws(IOException::class)
    val isDownloadProcess: Boolean
      get() = StopProcess.isFileExists()

    @JvmStatic
    fun startDownloadTask(url: String, path: String) {
      startDownloadTask(url.toURL(), path.toFile())
    }

    @JvmStatic
    fun startDownloadTask(url: String, file: File) {
      startDownloadTask(url.toURL(), file)
    }

    @JvmStatic
    fun startDownloadTask(url: URL, file: File) {
      val now = System.currentTimeMillis()
      val downloadUtils = DownloadUtils(file, url)
      try {
        logmaker.info("Task Start: $url")
        downloadUtils.manyTimesToTryDownload(5)
        logmaker.info("Task Finish: $file, Take A Period Of ${System.currentTimeMillis() - now} ms")
      } catch (e: Exception) {
        logmaker.warn("Task: $url", e)
        try {
          logmaker.info("Task: $url Start retry")
          downloadUtils.manyTimesToTryDownload(5)
          logmaker.info("Task Finish: $file, Take A Period Of ${System.currentTimeMillis() - now} ms")
        } catch (exception: Exception) {
          if (file.delete()) {
            logmaker.error("Task: $url Error", exception)
          }
        }
      }
    }
  }
}