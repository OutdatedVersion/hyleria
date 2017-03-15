package com.simplexitymc.kraken.damage.kinds;

import com.simplexitymc.kraken.damage.DamageEvent;
import com.simplexitymc.kraken.damage.DamageCause;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;

/**
 * OutdatedVersion
 * At: 10:42 PM (May/07/2016)
 * cave-realms
 */

public class StarvationDamageEvent extends DamageEvent
{
    public StarvationDamageEvent(Player victim, double dealtDamage)
    {
        super(victim, dealtDamage, DamageCause.ENVIRONMENT);
    }

    @Override
    public BaseComponent[] getInformation()
    {
        return new ComponentBuilder("").append("Food Deprivation - ").color(ChatColor.GRAY)
                .append(String.valueOf(getDamageDealt())).color(ChatColor.RED).create();
    }
}
