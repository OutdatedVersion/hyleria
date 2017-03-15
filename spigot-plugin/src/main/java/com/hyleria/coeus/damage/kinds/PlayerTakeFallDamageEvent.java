package com.simplexitymc.kraken.damage.kinds;

import com.simplexitymc.kraken.damage.DamageCause;
import com.simplexitymc.kraken.damage.DamageEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;

/**
 * OutdatedVersion
 * At: 11:11 PM (May/24/2016)
 * cave-realms
 */

public class PlayerTakeFallDamageEvent extends DamageEvent
{

    public PlayerTakeFallDamageEvent(Player victim, double damage)
    {
        super(victim, damage, DamageCause.ENVIRONMENT);
    }

    @Override
    public BaseComponent[] getInformation()
    {
        return new ComponentBuilder("").append("Fall Damage - ").color(ChatColor.GRAY)
                .append(String.valueOf(getDamageDealt())).color(ChatColor.RED).create();
    }

}
