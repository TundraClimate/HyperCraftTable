package io.github.tundraclimate.hypercrafttable.io.json

import com.google.gson.Gson
import io.github.tundraclimate.hypercrafttable.HyperCraftTable
import java.io.File
import java.io.FileReader

object RecipeJsonReader {
    private val gson = Gson()

    fun readJson(json: File): HyperCraftTable.RecipeJsonObject {
        if (json.extension != "json") throw IllegalArgumentException("invalid file")
        val reader = FileReader(json)
        return gson.fromJson(reader, HyperCraftTable.RecipeJsonObject::class.java)
    }
}