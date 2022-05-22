package io.github.tundraclimate.hypercrafttable.io.json

import io.github.tundraclimate.finelib.FineLib
import java.io.File

object RecipeReader {
    fun getShapedRecipeFiles(): List<File>? =
        File(FineLib.getPlugin().dataFolder, "shaped").listFiles()?.filter { it.extension == "json" }
    fun getShapelessRecipeFiles(): List<File>? =
        File(FineLib.getPlugin().dataFolder, "shapeless").listFiles()?.filter { it.extension == "json" }
}