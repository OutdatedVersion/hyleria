package com.hyleria.util;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.hyleria.util.Colors.bold;
import static org.bukkit.ChatColor.*;

/**
 * @author Ben (OutdatedVersion)
 * @since Feb/18/2017 (7:31 PM)
 */
public class PlayerUtil
{

    /**
     * @return the online player count
     */
    public static int onlineCount()
    {
        return Bukkit.getServer().getOnlinePlayers().length;
    }

    /**
     * @return a collection of every player who is online
     */
    public static List<? extends Player> everyone()
    {
        return Arrays.asList(Bukkit.getServer().getOnlinePlayers());
    }

    /**
     * @return a stream containing everyone that's online
     */
    public static Stream<? extends Player> everyoneStream()
    {
        return everyone().stream();
    }

    /**
     * Set's the provided player's health
     * to the maximum value it may be at
     * the time this is invoked.
     *
     * @param player the player
     */
    public static void fullHealth(Player player)
    {
        player.setHealth(player.getMaxHealth());
    }

    /**
     * Looks for a player matching the
     * name provided on the current server
     *
     * @param host   person looking for said player
     * @param target the player
     * @param inform whether or not to send updates regarding the status of the search
     *
     * @return the player or null
     */
    public static Player search(Player host, String target, boolean inform)
    {
        inform = inform && host != null;

        if (target.length() > 16)
        {
            if (inform)
                host.sendMessage(bold(YELLOW) + target + bold(GRAY) + " is too long! (>16 characters)");

            return null;
        }

        for (char character : target.toCharArray())
        {
            if (!Character.isLetterOrDigit(character) && character != '_')
            {
                if (inform)
                    host.sendMessage(bold(YELLOW) + target + bold(GRAY) + " is not a valid name!");

                return null;
            }
        }

        if (target.equalsIgnoreCase("me"))
            return host;


        final List<Player> _matches = everyoneStream().filter(player -> player.getName().toLowerCase().contains(target.toLowerCase())).collect(Collectors.toList());


        if (_matches.size() != 1)
        {
            if (inform)
            {
                if (_matches.size() != 0)
                    host.sendMessage(bold(GRAY) + "Matches for " + bold(YELLOW) + target + bold(GRAY) + " (" + bold(GREEN) + _matches.size() + bold(GRAY) + ")");

                if (_matches.size() > 0)
                {
                    final StringBuilder _builder = new StringBuilder();

                    for (Player working : _matches)
                        _builder.append(bold(YELLOW)).append(working.getName()).append(bold(GRAY)).append(", ");

                    // remove last ", "
                    if (_builder.length() > 1)
                        _builder.delete(_builder.length() - 2, _builder.length());

                    host.sendMessage(bold(GRAY) + "Matched names: " + _builder.toString());
                }
            }

            return null;
        }

        return _matches.get(0);
    }

    /**
     * Player the provided sound
     * to the specified player
     *
     * @param player the player
     * @param sound  the sound
     */
    public static void play(Player player, Sound sound)
    {
        player.playSound(player.getLocation(), sound, 100, 100);
    }

    /**
     * Play the provided sound to
     * every player online
     *
     * @param sound the sound
     */
    public static void play(Sound sound)
    {
        everyone().forEach(player -> play(player, sound));
    }

}
