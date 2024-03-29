package org.wdt.wdtc.core.download.forge

import com.google.gson.annotations.SerializedName
import org.wdt.utils.gson.parseJsonArray
import org.wdt.utils.gson.parseObject
import org.wdt.utils.io.toStrings
import org.wdt.wdtc.core.download.game.GameVersionsObjectList
import org.wdt.wdtc.core.download.infterface.VersionListInterface
import org.wdt.wdtc.core.download.infterface.VersionsJsonObjectInterface
import org.wdt.wdtc.core.game.*
import org.wdt.wdtc.core.utils.toURL
import java.io.IOException
import java.net.URL
import java.util.*

class ForgeVersionList(private val version: Version) : VersionListInterface {
  private val forgeListUrl: URL
    get() = "https://bmclapi2.bangbang93.com/forge/minecraft/${version.versionNumber}".toURL()

  @get:Throws(IOException::class)
  override val versionList: GameVersionsObjectList
    get() = GameVersionsObjectList().apply {
      forgeListUrl.toStrings().parseJsonArray().forEach {
        add(it.asJsonObject.parseObject<ForgeVersionsJsonObjectImpl>())
      }
    }


  class ForgeVersionsJsonObjectImpl(
    @field:SerializedName("version")
    override val versionNumber: String,

    @field:SerializedName("modified")
    val modified: Date,
  ) : VersionsJsonObjectInterface, Comparable<ForgeVersionsJsonObjectImpl> {

    @SerializedName("mcversion")
    val mcversion: String? = null
    override fun compareTo(other: ForgeVersionsJsonObjectImpl): Int {
      return modified.compareTo(other.modified).inv()
    }

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other == null || javaClass != other.javaClass) return false
      val that = other as ForgeVersionsJsonObjectImpl
      return versionNumber == that.versionNumber && mcversion == that.mcversion
    }

    override fun hashCode(): Int {
      return Objects.hash(versionNumber, mcversion)
    }
  }
}
