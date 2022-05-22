package io.github.tundraclimate.hypercrafttable.io.json

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.stream.JsonWriter
import io.github.tundraclimate.finelib.FineLib
import io.github.tundraclimate.finelib.util.Base64Converter
import io.github.tundraclimate.hypercrafttable.HyperCraftTable
import org.bukkit.inventory.ItemStack
import java.io.File
import java.io.FileWriter

object RecipeJsonWriter {
    private val gson = Gson()
    fun convert(result: ItemStack, table: List<ItemStack>): JsonElement {
        val map = mutableMapOf(
            "result" to Base64Converter.itemStackToBase64(result),
            "recipe" to Base64Converter.itemStacksToBase64(table.toTypedArray())
        )
        val obj = HyperCraftTable.RecipeJsonObject(map)
        return gson.toJsonTree(obj)
    }

    fun output(json: JsonElement) {
        val file = File(FineLib.getPlugin().dataFolder, "output.json")
        val jsonWriter = JsonWriter(FileWriter(file))
        gson.toJson(json, jsonWriter)
        jsonWriter.close()
        file.createNewFile()
    }
}