package com.hyleria.command.api.satisfier;

import com.hyleria.command.api.ArgumentSatisfier;
import com.hyleria.command.api.Arguments;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Returns whoever ran the command
 *
 * @author Ben (OutdatedVersion)
 * @since Mar/21/2017 (11:33 AM)
 */
public class ExecutedBySatisfier implements ArgumentSatisfier<Player>
{

    @Override
    public Player get(Player player, Arguments args)
    {
        return player;
    }

    @Override
    public String fail()
    {
        return ChatColor.RED + "How in the hell did this fail? Please contact a developer with a picture of this.";
    }

}
