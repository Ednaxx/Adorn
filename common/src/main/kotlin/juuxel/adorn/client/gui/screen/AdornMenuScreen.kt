package juuxel.adorn.client.gui.screen

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.MenuScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.menu.Menu
import net.minecraft.text.Text

abstract class AdornMenuScreen<M : Menu>(
    menu: M,
    playerInventory: PlayerInventory,
    title: Text
) : MenuScreen<M>(menu, playerInventory, title) {
    val panelX get() = x
    val panelY get() = y

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, tickDelta: Float) {
        renderBackground(context)
        super.render(context, mouseX, mouseY, tickDelta)
        drawMouseoverTooltip(context, mouseX, mouseY)
    }
}
