package kr.kro.chzzk.minecraft.message

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.event.EventHandler

@EventHandler
class PlayerMessage {
    fun getVersion(): String {
        val compoent: Component = Component.text("chzzk 0.1.0 by Hyeonproject")
            .color(TextColor.color(255, 255, 255))
            .append(Componet.text(" world!", NamedTextColor.GREEN))
    }
}