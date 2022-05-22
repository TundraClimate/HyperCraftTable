package io.github.tundraclimate.hypercrafttable

import io.github.tundraclimate.finelib.FineLib
import io.github.tundraclimate.hypercrafttable.io.json.RecipeJsonReader
import io.github.tundraclimate.hypercrafttable.io.json.RecipeReader
import io.github.tundraclimate.hypercrafttable.io.json.WriteJsonCommand
import io.github.tundraclimate.hypercrafttable.listener.InteractListener
import io.github.tundraclimate.hypercrafttable.listener.InventoryClickListener
import io.github.tundraclimate.hypercrafttable.listener.InventoryCloseListener
import io.github.tundraclimate.hypercrafttable.listener.InventoryOpenListener
import io.github.tundraclimate.hypercrafttable.server.CraftTableConfig
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import java.io.File
import java.util.*

val tasks = mutableMapOf<UUID, BukkitTask>()

val currentRecipes = mutableMapOf<UUID, Array<ItemStack?>>()

//既存のものを置き換えるんじゃなくて新しくGUIを作る。もしくは既存のものに戻すボタンを作成 <- 後者を現状は採用
class HyperCraftTable : JavaPlugin() {
    companion object {
        fun readShapedRecipes(): List<RecipeJsonObject>? {
            val shaped = RecipeReader.getShapedRecipeFiles() ?: return null
            val shapedRecipes = mutableListOf<RecipeJsonObject>()
            shaped.forEach { shapedRecipes.add(RecipeJsonReader.readJson(it)) }
            return shapedRecipes
        }

        fun readShapelessRecipes(): List<RecipeJsonObject>? {
            val shapeless = RecipeReader.getShapelessRecipeFiles() ?: return null
            val shapelessRecipes = mutableListOf<RecipeJsonObject>()
            shapeless.forEach { shapelessRecipes.add(RecipeJsonReader.readJson(it)) }
            return shapelessRecipes
        }
    }
    override fun onEnable() {
        FineLib.setPlugin(this)

        CraftTableConfig.init(this)
        WriteJsonCommand.init("writer")

        InteractListener.register()
        InventoryOpenListener.register()
        InventoryCloseListener.register()
        InventoryClickListener.register()

        File(dataFolder, "shaped").let { if (!it.exists()) it.mkdirs() }
        File(dataFolder, "shapeless").let { if (!it.exists()) it.mkdirs() }
    }

    data class RecipeJsonObject(val recipe: MutableMap<String, String?>)
}