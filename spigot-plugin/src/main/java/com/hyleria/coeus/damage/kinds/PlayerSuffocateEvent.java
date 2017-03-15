package com.hyleria.coeus.damage.kinds;

import com.hyleria.coeus.damage.DamageCause;
import com.hyleria.coeus.damage.DamageEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;

/**
 * When a player is stuck somewhere
 * and runs out of oxygen you'll
 * receive this.
 *
 * @author Ben (OutdatedVersion)
 * @since Aug/08/2016 (12:21 AM)
 */
public class PlayerSuffocateEvent extends DamageEvent
{

    public PlayerSuffocateEvent(Player victim, double damage)
    {
        super(victim, damage, DamageCause.ENVIRONMENT);
    }

    @Override
    public BaseComponent[] information()
    {
        return new ComponentBuilder("").append("Suffocation - ").color(ChatColor.GRAY)
                .append(String.valueOf(damageDealt())).color(ChatColor.RED).create();
    }

}
