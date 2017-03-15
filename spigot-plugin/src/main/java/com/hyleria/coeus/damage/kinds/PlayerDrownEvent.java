package com.simplexitymc.kraken.damage.kinds;

import com.simplexitymc.kraken.damage.DamageCause;
import com.simplexitymc.kraken.damage.DamageEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;

/**
 * OutdatedVersion
 * Aug/08/2016 (12:53 AM)
 */

public class PlayerDrownEvent extends DamageEvent
{

    public PlayerDrownEvent(Player victim, double damage)
    {
        super(victim, damage, DamageCause.ENVIRONMENT);
    }

    @Override
    public BaseComponent[] getInformation()
    {
        return new ComponentBuilder("").append("Drowning - ").color(ChatColor.GRAY)
                .append(String.valueOf(getDamageDealt())).color(ChatColor.RED).create();
    }

}
