package com.hyleria.coeus;

import com.google.inject.Inject;
import com.hyleria.command.api.Command;
import com.hyleria.command.api.SubCommand;
import com.hyleria.command.api.annotation.Permission;
import com.hyleria.common.reference.Role;
import org.bukkit.entity.Player;

import static com.hyleria.util.Colors.bold;
import static com.hyleria.util.TextUtil.formatEnum;
import static org.bukkit.ChatColor.*;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/23/2017 (1:32 PM)
 */
public class EngineCommands
{

    /** our game engine */
    @Inject private Coeus engine;

    @Command ( executor = "coeus" )
    public void baseCommand(Player player)
    {
        player.sendMessage(bold(GRAY) + "Currently playing " + bold(YELLOW) + engine.gameName());
    }

    @SubCommand ( of = "coeus", executors = { "status", "s" } )
    public void statusCheck(Player player)
    {
        player.sendMessage(bold(GRAY) + "Current game status: " + bold(YELLOW) + formatEnum(engine.status()));
    }

    @SubCommand ( of = "coeus", executors = "start" )
    @Permission ( Role.ADMIN )
    public void forceStart(Player player)
    {
        engine.startGame();
        player.sendMessage(bold(GREEN) + "You've started the game.");
    }

}
