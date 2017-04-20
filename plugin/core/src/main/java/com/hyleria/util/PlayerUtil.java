package com.hyleria.util;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
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
        return Arrays.asList(Bukkit.getServer().getOnlinePlayers()).stream();
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
     * @param host person looking for said player
     * @param target the player
     * @param inform whether or not to send updates regarding
     *               the status of the search
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
            if (!Character.isLetterOrDigit(character) && !String.valueOf(character).equals("_"))
            {
                if (inform)
                    host.sendMessage(bold(YELLOW) + target + bold(GRAY) + " is not a valid name!");

                return null;
            }
        }

        if (target.equalsIgnoreCase("me"))
            return host;

        final List<Player> _matches = Lists.newArrayList();

        for (Player players : everyone())
        {
            final String _name = players.getName();

            // :)
            if (_name.equals("OutdatedVersion") && target.equalsIgnoreCase("ben"))
                return players;

            if (_name.toLowerCase().contains(target.toLowerCase()))
                _matches.add(players);
        }

        if (_matches.size() != 1)
        {
            if (inform && _matches.size() != 0)
                host.sendMessage(bold(GRAY) + "Matches for " + bold(YELLOW) + target + bold(GRAY) + " (" + bold(GREEN) + _matches.size() + bold(GRAY) + ")");

            if (_matches.size() > 0)
            {
                String match = "";

                for (Player working : _matches)
                    match += bold(YELLOW) + working.getName() + bold(GRAY) + ", ";

                if (match.length() > 1)
                    match = match.substring(0, match.length() - 2);

                if (inform)
                    host.sendMessage(bold(GRAY) + "Matched names: " + match);
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
     * @param sound the sound
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
