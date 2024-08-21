
/*
 *  IsSlimeChunk - Check if you current chunk is a slime one!
 *  Copyright (C) 2020  Prof_Bloodstone
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
package dev.bloodstone.isslimechunk

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.command.PluginIdentifiableCommand
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.incendo.cloud.annotations.AnnotationParser
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.CommandDescription
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.bukkit.CloudBukkitCapabilities
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.paper.LegacyPaperCommandManager

public class IsSlimeChunk() : JavaPlugin() {
    private lateinit var manager: LegacyPaperCommandManager<CommandSender>
    private lateinit var annotationParser: AnnotationParser<CommandSender>

    override fun onEnable() {
        manager = LegacyPaperCommandManager.createNative(this, ExecutionCoordinator.simpleCoordinator())

        if (manager.hasCapability(CloudBukkitCapabilities.BRIGADIER)) {
            manager.registerBrigadier()
        }
        annotationParser = AnnotationParser(manager, CommandSender::class.java)
        // Parse commands
        annotationParser.parse(this)
    }

    override fun onDisable() {
        Bukkit.getCommandMap().knownCommands.entries
            .removeIf { entry -> (entry.value as? PluginIdentifiableCommand)?.plugin == this }
    }

    @Command("isSlimeChunk|slime?")
    @CommandDescription("Check whether chunk is a slime chunk")
    @Permission("isslimechunk.self")
    private fun isSlimeChunk(player: Player) {
        val isSlime = player.chunk.isSlimeChunk
        player.sendMessage("This chunk ${ if (isSlime) "is" else "is NOT" } a slime chunk.")
    }
}
