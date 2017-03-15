package com.simplexitymc.kraken.damage.kinds;

import com.simplexitymc.kraken.damage.DamageEvent;
import com.simplexitymc.kraken.damage.DamageCause;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;

/**
 * OutdatedVersion
 * At: 10:41 PM (May/07/2016)
 * cave-realms
 */

public class LavaDamageEvent extends DamageEvent
{

    public LavaDamageEvent(Player victim, double dealtDamage)
    {
        super(victim, dealtDamage, DamageCause.ENVIRONMENT);
    }

    @Override
    public BaseComponent[] getInformation()
    {
        return new ComponentBuilder("").append("Lava - ").color(ChatColor.GRAY)
                .append(String.valueOf(getDamageDealt())).color(ChatColor.RED).create();
    }

}
