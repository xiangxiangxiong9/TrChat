package me.arasple.mc.trchat.util.color

import org.bukkit.command.CommandSender

/**
 * @author ItsFlicker
 * @since 2021/12/12 12:30
 */
class CustomColor(val type: ColorType, val color: String) {

    enum class ColorType {

        NORMAL, SPECIAL
    }

    fun colored(sender: CommandSender, msg: String): String {
        var message = MessageColors.replaceWithPermission(sender, msg)

        if (!msg.startsWith("§")) {
            message = when (type) {
                ColorType.NORMAL -> color + message
                ColorType.SPECIAL -> (color + message).parseRainbow().parseGradients()
            }
        }

        return message
    }

    companion object {

        private val caches = mutableMapOf<String, CustomColor>()

        fun get(string: String): CustomColor {
            return caches.computeIfAbsent(string) {
                val type = if (HexUtils.GRADIENT_PATTERN.matcher(it).find() || HexUtils.RAINBOW_PATTERN.matcher(it).find()) {
                    ColorType.SPECIAL
                } else {
                    ColorType.NORMAL
                }
                val color = if (type == ColorType.NORMAL) {
                    if (it.length == 1) "§$it" else it.parseHex().parseLegacy()
                } else {
                    it
                }
                CustomColor(type, color)
            }
        }
    }
}