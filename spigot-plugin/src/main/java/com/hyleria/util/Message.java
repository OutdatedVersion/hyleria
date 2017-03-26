package com.hyleria.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/25/2017 (4:38 PM)
 */
public class Message
{

    /** the builder backing this message */
    private ComponentBuilder builder;

    /**
     * @param prefix the prefix of the message
     */
    private Message(String prefix)
    {
        this.builder = new ComponentBuilder(prefix + " »").color(ChatColor.DARK_AQUA);
    }

    /**
     * @param prefix the prefix
     * @return a new message builder
     */
    public static Message prefix(String prefix)
    {
        return new Message(prefix);
    }

    /**
     * @param text the text
     * @return this builder
     */
    public Message content(String text)
    {
        return content(text, ChatColor.GRAY);
    }

    /**
     * @param text the text to add
     * @param color color of the text
     * @return this builder
     */
    public Message content(String text, ChatColor color)
    {
        builder.append(" ").append(text).color(color);
        return this;
    }

    /**
     * @param player the player to send
     *               the message (at it's
     *               current state) to
     * @return the player
     */
    public Player send(Player player)
    {
        player.spigot().sendMessage(builder.append(".").color(ChatColor.GRAY).create());
        return player;
    }

    /**
     * @param player the player to send the message to
     * @return the player it was sent to
     */
    public Player sendAsIs(Player player)
    {
        player.spigot().sendMessage(builder.create());
        return player;
    }

}
