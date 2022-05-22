package io.github.tundraclimate.hypercrafttable.server

import io.github.tundraclimate.finelib.util.ConfigObject

object CraftTableConfig: ConfigObject() {
    fun isClickToOpen() = config.getBoolean("click_to_open_gui", true)
}