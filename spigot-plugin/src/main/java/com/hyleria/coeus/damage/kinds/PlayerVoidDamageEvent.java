package com.simplexitymc.kraken.damage.kinds;

import com.simplexitymc.kraken.damage.DamageCause;
import com.simplexitymc.kraken.damage.DamageEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;

/**
 * OutdatedVersion
 * 9:31 PM (Jul/31/2016)
 */

public class PlayerVoidDamageEvent extends DamageEvent
{

    public PlayerVoidDamageEvent(Player victim, double dealtDamage)
    {
        super(victim, dealtDamage, DamageCause.ENVIRONMENT);
    }

    @Override
    public BaseComponent[] getInformation()
    {
        return new ComponentBuilder("").append("World Void - ").color(ChatColor.GRAY)
                .append(String.valueOf(getDamageDealt())).color(ChatColor.RED).create();
    }

}
