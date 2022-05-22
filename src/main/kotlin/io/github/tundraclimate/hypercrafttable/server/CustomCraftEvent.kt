package io.github.tundraclimate.hypercrafttable.server

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class CustomCraftEvent(
    val player: Player,
    val result: ItemStack,
    val recipe: Array<ItemStack?>,
    val craftTable: Inventory,
    val shiftClicked: Boolean
) : Event() {
    companion object {
        private val HANDLER_LIST = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLER_LIST
    }

    override fun getHandlers(): HandlerList = HANDLER_LIST
}