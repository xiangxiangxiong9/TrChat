package me.arasple.mc.trchat.internal.proxy

import me.arasple.mc.trchat.api.TrChatFiles
import me.arasple.mc.trchat.internal.proxy.bungee.Bungees
import me.arasple.mc.trchat.internal.proxy.velocity.Velocity
import org.bukkit.entity.Player
import taboolib.common.platform.Platform
import taboolib.common.platform.PlatformSide
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang

/**
 * Proxy
 * me.arasple.mc.trchat.internal.proxy
 *
 * @author wlys
 * @since 2021/8/21 13:24
 */
@PlatformSide([Platform.BUKKIT])
object Proxy {

    var isEnabled = false

    val platform by lazy {
        when (val p = TrChatFiles.settings.getString("GENERAL.PROXY", "NONE").uppercase()) {
            "NONE" -> {
                console().sendLang("Plugin-Proxy-None")
                Platform.UNKNOWN
            }
            "BUNGEE" -> {
                console().sendLang("Plugin-Proxy-Bungee")
                Platform.BUNGEE
            }
            "VELOCITY" -> {
                console().sendLang("Plugin-Proxy-Velocity")
                Platform.VELOCITY
            }
            else -> error("Unsupported proxy $p.")
        }
    }

    fun init() {
        when (platform) {
            Platform.BUNGEE -> Bungees.init()
            Platform.VELOCITY -> Velocity.init()
            else -> return
        }
    }

    fun sendProxyData(player: Player, vararg args: String) {
        when (platform) {
            Platform.BUNGEE -> Bungees.sendBungeeData(player, *args)
            Platform.VELOCITY -> Velocity.sendVelocityData(player, *args)
            else -> return
        }
    }
}