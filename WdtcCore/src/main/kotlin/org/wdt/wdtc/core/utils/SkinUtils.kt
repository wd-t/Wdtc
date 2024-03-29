package org.wdt.wdtc.core.utils

import org.wdt.utils.io.*
import org.wdt.wdtc.core.manger.userAsste
import java.io.File
import java.io.IOException
import java.io.InputStream
import javax.imageio.ImageIO

class SkinUtils(
	private var skinFile: File
) {
	
	var userName: String? = null
	
	var userSkinInput: InputStream? = null
	
	@Throws(IOException::class)
	fun writeSkinHead(): File {
		val newImage = ImageIO.read(userSkinInput ?: skinFile.newInputStream()).getSubimage(8, 8, 8, 8)
		return skinFile.extension.let {
			File(
				userAsste, skinFile.name.cleanStrInString(".$it").appendForString("-head.", it)
			).apply {
				launchScope("Write skin file") {
					touch()
					ImageIO.write(newImage, extension, outputStream())
				}
			}
		}
	}
	
	fun copySkinFile() {
		IOUtils.copy(userSkinInput, getUserSkinFile(userName).newOutputStream())
	}
	
	companion object {
		fun getUserSkinFile(userName: String?): File {
			return File(userAsste, "$userName.png")
		}
	}
}
