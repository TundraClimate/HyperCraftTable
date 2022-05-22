package io.github.tundraclimate.hypercrafttable.ui

import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
 object HyperCraftTableHolder: InventoryHolder {
    override fun getInventory(): Inventory = Bukkit.createInventory(null, 27)
}