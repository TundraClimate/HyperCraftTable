package io.github.tundraclimate.hypercrafttable.listener

import io.github.tundraclimate.finelib.addon.server.RegisterEvent
import io.github.tundraclimate.hypercrafttable.server.CraftTableConfig
import io.github.tundraclimate.hypercrafttable.ui.HyperCraftTableUI
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

object InteractListener: RegisterEvent {
    @EventHandler
    private fun onInteract(e: PlayerInteractEvent) {
        if (e.action != Action.RIGHT_CLICK_BLOCK) return
        if (!CraftTableConfig.isClickToOpen()) return
        if (e.clickedBlock?.type != Material.CRAFTING_TABLE) return
        e.isCancelled = true
        e.player.openInventory(HyperCraftTableUI.create())
    }
}