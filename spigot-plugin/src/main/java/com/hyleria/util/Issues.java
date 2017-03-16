package com.hyleria.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.entity.Player;

 /**
  * @author Ben (OutdatedVersion)
  * @since Dec/23/2016 (3:44 PM)
  */
public class Issues
{

    /**
     * Process some thrown exception that happened
     * somewhere on the server. We use this so we
     * may display them in-game to OPs.
     *
     * @param friendlyName some unique identifier
     *                     that we won't be confused
     *                     by when reading
     * @param throwable what went wrong
     */
    public static void handle(String friendlyName, Throwable throwable)
    {
        // terminal..
        throwable.printStackTrace();

        // in-game..
        final StackTraceElement _head = throwable.getStackTrace()[0];

        final ComponentBuilder _builder = new ComponentBuilder("");
        _builder.append("k").obfuscated(true);
        _builder.append(" ERROR ").color(ChatColor.DARK_RED).bold(true).obfuscated(false);

        TextComponent _hoverText = new TextComponent(String.format("§7§lFriendly Name: §a%s\n§7Location: §e%s\n§7Method: §e%s\n§7Line: §c%s",
                                                                   friendlyName,
                                                                   _head.getClassName(),
                                                                   _head.getMethodName(),
                                                                   _head.getLineNumber()));

        _builder.append("@ ").color(ChatColor.RED).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[] { _hoverText }));

        _builder.append(throwable.toString()).color(ChatColor.LIGHT_PURPLE)
                .event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://duckduckgo.com/?q=" + throwable.toString()))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[] { new TextComponent("§7Click to search for this exception") }));

        final BaseComponent[] _message = _builder.create();

        PlayerUtil.everyoneStream().filter(Player::isOp).forEach(recipient -> recipient.spigot().sendMessage(_message));
    }

}
