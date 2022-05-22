package io.github.tundraclimate.hypercrafttable.ui

import io.github.tundraclimate.finelib.builder.InventoryBuilder
import io.github.tundraclimate.finelib.builder.ItemStackBuilder
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.persistence.PersistentDataType

object HyperCraftTableUI {
    private val blackPane = ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE)
        .setDisplayName(" ")
        .addContainerData("ct.c", PersistentDataType.STRING, "ct.c")
        .toItemStack()
    private val changeButton = ItemStackBuilder(Material.ARROW)
        .setDisplayName("Change Default GUI")
        .addContainerData("ct.c", PersistentDataType.STRING, "ct.c")
        .addContainerData("ct.e", PersistentDataType.STRING, "ct.e")
        .toItemStack()
    fun create(): Inventory = InventoryBuilder(HyperCraftTableHolder, 3, "craftTable")
        .fillItem(blackPane)
        .remove(1).remove(2).remove(3)
        .remove(10).remove(11).remove(12)
        .remove(19).remove(20).remove(21)
        .remove(15)
        .setItem(26, changeButton)
        .toInventory()
}