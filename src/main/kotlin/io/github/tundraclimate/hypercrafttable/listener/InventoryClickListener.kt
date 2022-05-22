package io.github.tundraclimate.hypercrafttable.listener

import io.github.tundraclimate.finelib.FineLib
import io.github.tundraclimate.finelib.addon.server.RegisterEvent
import io.github.tundraclimate.hypercrafttable.currentRecipes
import io.github.tundraclimate.hypercrafttable.io.json.RecipeJsonWriter
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.CraftingInventory
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object InventoryClickListener : RegisterEvent {
    @EventHandler
    private fun onInventoryClick(e: InventoryClickEvent) {
        val currentContainer = e.currentItem?.itemMeta?.persistentDataContainer

        //レシピリザルトのクリック判定
        if (e.clickedInventory?.indexOf(e.currentItem) == 15 && currentContainer?.has(
                NamespacedKey(
                    FineLib.getPlugin(),
                    "ct.empty"
                ), PersistentDataType.STRING
            ) != true
        ) {
            //レシピとテーブル詰める
            val currentRecipe = currentRecipes[e.whoClicked.uniqueId] ?: return
            val table = e.clickedInventory?.contents?.let {
                arrayOf<ItemStack?>(
                    it[1],
                    it[2],
                    it[3],
                    it[10],
                    it[11],
                    it[12],
                    it[19],
                    it[20],
                    it[21]
                )
            } ?: return

            //クラフト処理
            fun craft() {
                (0..8).forEach {
                    (table[it] ?: ItemStack(Material.AIR)).amount -= (currentRecipe[it]
                        ?: ItemStack(Material.AIR)).amount
                }
            }

            //クラフト判定
            fun isCraft(): Boolean {
                val list = mutableListOf<Boolean>()
                (0..8).forEach {
                    list.add(
                        when {
                            (table[it] ?: ItemStack(Material.AIR)).amount >= (currentRecipe[it]
                                ?: ItemStack(Material.AIR)).amount -> true
                            else -> false
                        }
                    )
                }
                return list.filter { it }.size == 9
            }
            craft()
            val cursor = e.cursor ?: return
            val currentItem = e.currentItem ?: return
            //シフトクリック
            if (e.click.isShiftClick) {
                val inv = e.whoClicked.inventory
                val result = e.currentItem?.clone()
                while (true) {
                    if (!isCraft()) break
                    craft()
                    inv.addItem(result)
                }
            }
            //アイテム重ねる
            if (cursor.isSimilar(currentItem)) {
                e.isCancelled = true

                cursor.amount += currentItem.amount
            }
        }


        if (
            currentContainer?.has(NamespacedKey(FineLib.getPlugin(), "ct.c"), PersistentDataType.STRING) == true
        ) {
            //クリックのキャンセル
            e.isCancelled = true
            e.result = Event.Result.DENY
            if (currentContainer.has(NamespacedKey(FineLib.getPlugin(), "ct.e"), PersistentDataType.STRING)) {
                val ct = e.whoClicked.openWorkbench(null, true)?.topInventory
            }

            //Json書き込み処理
            if (
                currentContainer.has(
                    NamespacedKey(FineLib.getPlugin(), "ct.writer"),
                    PersistentDataType.STRING
                )
            ) {
                //Json出力
                val check = writableCheck(e.clickedInventory!!)
                val clicker = e.whoClicked
                val checking = listOf(1, 2, 3, 10, 11, 12, 19, 20, 21)
                val table = mutableListOf<ItemStack>()
                val result = e.clickedInventory!!.getItem(15)
                checking.forEach { table.add(e.clickedInventory!!.getItem(it) ?: ItemStack(Material.AIR)) }
                clicker.inventory.addItem(result ?: ItemStack(Material.AIR))
                table.forEach { clicker.inventory.addItem(it) }
                //ここのif..elseでJsonの出力
                if (check.first) {
                    clicker.closeInventory()
                    clicker.sendMessage(check.second)
                    RecipeJsonWriter.output(RecipeJsonWriter.convert(result ?: ItemStack(Material.AIR), table))
                } else {
                    clicker.closeInventory()
                    clicker.sendMessage(check.second)
                }
            }
        }
    }

    private fun writableCheck(inv: Inventory): Pair<Boolean, String> {
        val checking = listOf(1, 2, 3, 10, 11, 12, 19, 20, 21)
        val table = mutableListOf<ItemStack>()
        inv.getItem(15) ?: return false to "§cPlease Set Result"
        checking.forEach { table.add(inv.getItem(it) ?: ItemStack(Material.AIR)) }
        if (table.filter { it.type == Material.AIR }.size == 9) return false to "§cPlease Set Recipe"
        return true to "§aWrite And Output"
    }
}