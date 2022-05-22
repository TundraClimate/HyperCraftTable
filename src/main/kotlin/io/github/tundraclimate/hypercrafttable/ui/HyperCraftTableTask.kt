package io.github.tundraclimate.hypercrafttable.ui

import io.github.tundraclimate.finelib.builder.ItemStackBuilder
import io.github.tundraclimate.finelib.util.Base64Converter
import io.github.tundraclimate.hypercrafttable.HyperCraftTable
import io.github.tundraclimate.hypercrafttable.currentRecipes
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object HyperCraftTableTask {
    private val barrier = ItemStackBuilder(Material.BARRIER)
        .setDisplayName("§cEmpty")
        .addContainerData("ct.c", PersistentDataType.STRING, "ct.c")
        .addContainerData("ct.empty", PersistentDataType.STRING, "ct.empty")
        .toItemStack()

    fun getTask(player: Player, inv: Inventory): Runnable = Runnable {
        var clear = false
        fun setShapedRecipe() {
            //レシピを取り出す
            val shapedRecipes = HyperCraftTable.readShapedRecipes() ?: return
            val base64RecipeMapList = mutableListOf<Pair<String, String>>()
            val recipeMapList = mutableListOf<Pair<ItemStack, Array<ItemStack?>>>()
            //最終結果
            var craftResult = barrier
            //詰め替え
            shapedRecipes.forEach {
                base64RecipeMapList.add(
                    (it.recipe["result"] ?: return@forEach) to (it.recipe["recipe"] ?: return@forEach)
                )
            }
            base64RecipeMapList.forEach {
                val result = Base64Converter.base64ToItemStack(it.first) ?: return@forEach
                val recipe = Base64Converter.base64ToItemStacks(it.second)
                recipe.forEachIndexed() { i, r ->
                    if (r?.type == Material.AIR) recipe[i] = null
                }
                recipeMapList.add(result to recipe)
            }

            //現在のクラフトテーブルも取り出す
            val table: List<ItemStack?> =
                inv.contents.let { listOf(it[1], it[2], it[3], it[10], it[11], it[12], it[19], it[20], it[21]) }

            recipeMapList.forEach {
                val currentRecipe = it.second
                //テーブルとレシピを比較
                if (equalArray(currentRecipe, table)) {
                    craftResult = it.first
                    currentRecipes[player.uniqueId] = it.second
                    clear = true
                }
                inv.setItem(15, craftResult)
            }
        }

        fun setShapelessRecipe() {
            //レシピを取り出す
            val shapedRecipes = HyperCraftTable.readShapelessRecipes() ?: return
            val base64RecipeMapList = mutableListOf<Pair<String, String>>()
            val recipeMapList = mutableListOf<Pair<ItemStack, Array<ItemStack?>>>()
            //最終結果
            var craftResult = barrier
            //詰め替え
            shapedRecipes.forEach {
                base64RecipeMapList.add(
                    (it.recipe["result"] ?: return@forEach) to (it.recipe["recipe"] ?: return@forEach)
                )
            }
            base64RecipeMapList.forEach {
                val result = Base64Converter.base64ToItemStack(it.first) ?: return@forEach
                val recipe = Base64Converter.base64ToItemStacks(it.second)
                recipe.forEachIndexed() { i, r ->
                    if (r?.type == Material.AIR) recipe[i] = null
                }
                recipeMapList.add(result to recipe)
            }

            //現在のクラフトテーブルも取り出す
            val table: List<ItemStack?> =
                inv.contents.let {
                    listOf(
                        it[1]?.clone(),
                        it[2]?.clone(),
                        it[3]?.clone(),
                        it[10]?.clone(),
                        it[11]?.clone(),
                        it[12]?.clone(),
                        it[19]?.clone(),
                        it[20]?.clone(),
                        it[21]?.clone()
                    )
                }

            //nullを消して各個数を1に
            val oneTable = mutableListOf<ItemStack?>()
            table.map { it?.amount = 1; it }.forEach { oneTable.add(it) }
            val oneTableNotNull = oneTable.filterNotNull()
            recipeMapList.forEach { pair ->
                //レシピも
                val recipeNotNull = mutableListOf<ItemStack>()
                pair.second.filterNotNull().forEach { recipeNotNull.add(it) }
                //ソートしてから比較
                if (oneTableNotNull.sortedBy { it.hashCode() } == recipeNotNull.sortedBy { it.hashCode() }) {
                    //mapに渡す
                    craftResult = pair.first
                    currentRecipes[player.uniqueId] = oneTable.toTypedArray()
                }
                inv.setItem(15, craftResult)
            }
        }

        setShapedRecipe()
        if (!clear)
            setShapelessRecipe()
    }

    //テーブルとレシピ比較用
    private fun equalArray(recipe: Array<ItemStack?>, table: List<ItemStack?>): Boolean {
        return recipe[0].equalsOfAmount(table[0]) && recipe[1].equalsOfAmount(table[1]) &&
                recipe[2].equalsOfAmount(table[2]) && recipe[3].equalsOfAmount(table[3]) &&
                recipe[4].equalsOfAmount(table[4]) && recipe[5].equalsOfAmount(table[5]) &&
                recipe[6].equalsOfAmount(table[6]) && recipe[7].equalsOfAmount(table[7]) &&
                recipe[8].equalsOfAmount(table[8])
    }

    private fun ItemStack?.equalsOfAmount(table: ItemStack?): Boolean {
        val recipe = this
        return when {
            recipe == null && table == null -> true
            recipe == null -> false
            table == null -> false
            recipe == table -> true
            recipe.isSimilar(table) && recipe.amount <= table.amount -> true
            else -> false
        }
    }
}