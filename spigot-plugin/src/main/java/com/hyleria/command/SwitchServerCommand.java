package com.hyleria.command;

import com.google.inject.Inject;
import com.hyleria.command.api.Command;
import com.hyleria.command.api.annotation.Necessary;
import com.hyleria.common.backend.ServerConfig;
import com.hyleria.common.backend.payload.SwitchPlayerServerPayload;
import com.hyleria.common.redis.RedisHandler;
import com.hyleria.util.Message;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.entity.Player;

import static net.md_5.bungee.api.ChatColor.GREEN;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/25/2017 (4:19 PM)
 */
public class SwitchServerCommand
{

    /** the server that we're on */
    private final String serverName;

    /** our redis instance */
    @Inject private RedisHandler redis;

    @Inject
    public SwitchServerCommand(ServerConfig config)
    {
        this.serverName = config.name;
    }

    @Command ( executor = { "server", "s" } )
    public void run(Player player, @Necessary ( "You must provide a server to switch to" ) String server)
    {
        server = formatServerName(server);

        if (server.equals(serverName))
        {
            Message.prefix("Network").content("You're already connected to")
                                     .content(server, GREEN).send(player);
            return;
        }

        Message.prefix("Network").content("You're being connected to").content(server, GREEN).send(player);
        new SwitchPlayerServerPayload(player.getUniqueId(), server).publish(redis);
    }

    @Command ( executor = { "whereami", "where" } )
    public void whereAmI(Player player)
    {
        Message.prefix("Network").content("You're currently connected to").content(serverName, GREEN).send(player);
    }

    /**
     * thank you cookiez
     *
     * @param name the name inputted
     * @return a formatted name
     */
    private static String formatServerName(String name)
    {
        if (name.startsWith("-"))
            name = name.substring(1);
        else if (name.endsWith("-"))
            name += "1";
        else
        {
            String[] _tokens = name.split("-");

            if (_tokens.length == 1 || _tokens.length == 0)
                name += "-1";
            else if (_tokens.length == 2 && ! NumberUtils.isNumber(_tokens[1]))
                name = name.substring(0, name.length() - (1 + _tokens[1].length()));
            else if (name.split("-").length > 2)
            {
                if (NumberUtils.isCreatable(_tokens[1]))
                    name = _tokens[0] + "-" + _tokens[1];
                else
                    name = _tokens[0] + "-1";
            }

            if (!name.contains("-"))
                name += "-1";
        }

        return name;
    }

}
