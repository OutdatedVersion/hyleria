package com.hyleria.util;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.entity.Player;

import static com.hyleria.util.Colors.bold;
import static java.lang.String.format;
import static org.bukkit.ChatColor.WHITE;
import static org.bukkit.ChatColor.YELLOW;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/15/2017 (12:08 AM)
 */
public class MessageUtil
{

    /** empty hover event */
    public static final HoverEvent EMPTY_HOVER = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[] { } );

    /** empty click event */
    public static final ClickEvent EMPTY_CLICK = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "");

    /**
     * Sends the provided message to
     * every online player.
     *
     * @param message the message
     */
    public static void everyone(final String message)
    {
        PlayerUtil.everyone().forEach(player -> player.sendMessage(message));
    }

    /**
     * @param thing prefix
     * @param val value
     */
    public static void debug(String thing, Object val)
    {
        final String _message = bold(YELLOW) + format("[%s]: ", thing) + WHITE + String.valueOf(val);

        PlayerUtil.everyoneStream().filter(Player::isOp).forEach(player -> player.sendMessage(_message));
    }

}
