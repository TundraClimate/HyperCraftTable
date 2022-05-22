package io.github.tundraclimate.hypercrafttable.ui

import io.github.tundraclimate.finelib.builder.InventoryBuilder
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

object WriterHolder: InventoryHolder {
    override fun getInventory(): Inventory {
        return InventoryBuilder(3).toInventory()
    }
}