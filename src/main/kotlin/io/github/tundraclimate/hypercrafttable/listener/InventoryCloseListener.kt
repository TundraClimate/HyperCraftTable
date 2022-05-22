package io.github.tundraclimate.hypercrafttable.listener

import io.github.tundraclimate.finelib.addon.server.RegisterEvent
import io.github.tundraclimate.hypercrafttable.tasks
import io.github.tundraclimate.hypercrafttable.ui.HyperCraftTableHolder
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

object InventoryCloseListener : RegisterEvent {
    @EventHandler
    private fun onInventoryClose(e: InventoryCloseEvent) {
        if (e.inventory.holder == null || e.inventory.holder != HyperCraftTableHolder) return
        tasks[e.player.uniqueId]?.cancel()
        arrayOf(1, 2, 3, 10, 11, 12, 19, 20, 21).forEach {
            e.player.inventory.addItem(
                e.inventory.contents[it] ?: ItemStack(Material.AIR)
            )
        }
        tasks.remove(e.player.uniqueId)
    }
}