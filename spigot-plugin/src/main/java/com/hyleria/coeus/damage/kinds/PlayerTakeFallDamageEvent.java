package com.hyleria.coeus.damage.kinds;

import com.hyleria.coeus.damage.DamageCause;
import com.hyleria.coeus.damage.DamageEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;

/**
 * Whenever a player takes fall
 * damage this will be fired off.
 *
 * @author Ben (OutdatedVersion)
 * @since 11:11 PM (May/24/2016)
 */
public class PlayerTakeFallDamageEvent extends DamageEvent
{

    public PlayerTakeFallDamageEvent(Player victim, double damage)
    {
        super(victim, damage, DamageCause.ENVIRONMENT);
    }

    @Override
    public BaseComponent[] information()
    {
        return new ComponentBuilder("").append("Fall Damage - ").color(ChatColor.GRAY)
                .append(String.valueOf(damageDealt())).color(ChatColor.RED).create();
    }

}
