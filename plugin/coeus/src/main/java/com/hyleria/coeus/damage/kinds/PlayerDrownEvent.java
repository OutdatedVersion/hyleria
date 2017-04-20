package com.hyleria.coeus.damage.kinds;

import com.hyleria.coeus.damage.DamageCause;
import com.hyleria.coeus.damage.DamageEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;

/**
 * As the player looses their oxygen
 * they'll die; when they die this event
 * will be fired off.
 *
 * @author Ben (OutdatedVersion)
 * @since Aug/08/2016 (12:53 AM)
 */
public class PlayerDrownEvent extends DamageEvent
{

    public PlayerDrownEvent(Player victim, double damage)
    {
        super(victim, damage, DamageCause.ENVIRONMENT);
    }

    @Override
    public BaseComponent[] information()
    {
        return new ComponentBuilder("").append("Drowning - ").color(ChatColor.GRAY)
                .append(String.valueOf(damageDealt())).color(ChatColor.RED).create();
    }

}
