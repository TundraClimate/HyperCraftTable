package io.github.tundraclimate.hypercrafttable.listener

import io.github.tundraclimate.finelib.FineLib
import io.github.tundraclimate.finelib.addon.server.RegisterEvent
import io.github.tundraclimate.hypercrafttable.tasks
import io.github.tundraclimate.hypercrafttable.ui.HyperCraftTableHolder
import io.github.tundraclimate.hypercrafttable.ui.HyperCraftTableTask
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryOpenEvent

object InventoryOpenListener : RegisterEvent {
    @EventHandler
    private fun onInventoryOpen(e: InventoryOpenEvent) {
        if (e.inventory.holder == null || e.inventory.holder != HyperCraftTableHolder) return
        val task = Bukkit.getScheduler()
            .runTaskTimer(FineLib.getPlugin(), HyperCraftTableTask.getTask(e.player as Player, e.inventory), 0L, 0L)
        tasks[e.player.uniqueId] = task
    }
}