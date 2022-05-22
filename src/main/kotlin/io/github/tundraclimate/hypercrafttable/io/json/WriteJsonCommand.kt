package io.github.tundraclimate.hypercrafttable.io.json

import io.github.tundraclimate.finelib.addon.server.InitializeTabExecutor
import io.github.tundraclimate.finelib.builder.InventoryBuilder
import io.github.tundraclimate.finelib.builder.ItemStackBuilder
import io.github.tundraclimate.hypercrafttable.ui.WriterHolder
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

object WriteJsonCommand: InitializeTabExecutor {
    private val blackPane = ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE)
        .setDisplayName(" ")
        .addContainerData("ct.c", PersistentDataType.STRING, "ct.c")
        .toItemStack()
    private val button = ItemStackBuilder(Material.RED_STAINED_GLASS_PANE)
        .setDisplayName("§cWrite")
        .setLore("§eWrite Json on Click")
        .addContainerData("ct.c", PersistentDataType.STRING, "ct.c")
        .addContainerData("ct.writer", PersistentDataType.STRING, "ct.writer")
        .toItemStack()

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (command.name.equals("writer", true)) {
            if (sender !is Player) return false
            val inv = InventoryBuilder(WriterHolder, 3, "writer")
                .fillItem(blackPane)
                .remove(1).remove(2).remove(3)
                .remove(10).remove(11).remove(12)
                .remove(19).remove(20).remove(21)
                .remove(15).setItem(16, button)
                .toInventory()
            sender.openInventory(inv)
        }
        return false
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): MutableList<String>? {
        return null
    }
}