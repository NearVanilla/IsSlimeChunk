
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

import cloud.commandframework.annotations.AnnotationParser
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission
import cloud.commandframework.arguments.parser.ParserParameters
import cloud.commandframework.arguments.parser.StandardParameters
import cloud.commandframework.bukkit.CloudBukkitCapabilities
import cloud.commandframework.execution.CommandExecutionCoordinator
import cloud.commandframework.meta.CommandMeta
import cloud.commandframework.paper.PaperCommandManager
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.command.PluginIdentifiableCommand
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.function.Function

public class IsSlimeChunk() : JavaPlugin() {
    private lateinit var manager: PaperCommandManager<CommandSender>
    private lateinit var annotationParser: AnnotationParser<CommandSender>

    override fun onEnable() {
        manager = PaperCommandManager(
            this,
            CommandExecutionCoordinator.simpleCoordinator(), // Do command execution synchronously
            Function.identity(),
            Function.identity(),
        )
        if (manager.queryCapability(CloudBukkitCapabilities.BRIGADIER)) {
            manager.registerBrigadier()
        }
        annotationParser = AnnotationParser(
            manager,
            CommandSender::class.java
        ) { parserParams: ParserParameters ->
            CommandMeta.simple()
                .with(CommandMeta.DESCRIPTION, parserParams.get(StandardParameters.DESCRIPTION, "No description"))
                .build()
        }
        // Parse commands
        annotationParser.parse(this)
    }

    override fun onDisable() {
        Bukkit.getCommandMap().knownCommands.entries
            .removeIf { entry -> (entry.value as? PluginIdentifiableCommand)?.plugin == this }
    }

    @CommandMethod("isSlimeChunk|slime?")
    @CommandDescription("Check whether chunk is a slime chunk")
    @CommandPermission("isslimechunk.self")
    private fun isSlimeChunk(player: Player) {
        val isSlime = player.chunk.isSlimeChunk
        player.sendMessage("This chunk ${ if (isSlime) "is" else "is NOT" } a slime chunk.")
    }
}
