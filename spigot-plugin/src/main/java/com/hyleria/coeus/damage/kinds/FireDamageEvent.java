package com.hyleria.coeus.damage.kinds;

import com.hyleria.coeus.damage.DamageCause;
import com.hyleria.coeus.damage.DamageEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;

/**
 * Damage done by fire
 *
 * @author Ben (OutdatedVersion)
 * @since 10:38 PM (May/07/2016)
 */
public class FireDamageEvent extends DamageEvent
{

    public FireDamageEvent(Player victim, double dealtDamage)
    {
        super(victim, dealtDamage, DamageCause.ENVIRONMENT);
    }

    @Override
    public BaseComponent[] information()
    {
        return new ComponentBuilder("").append("Fire - ").color(ChatColor.GRAY)
                  .append(String.valueOf(damageDealt())).color(ChatColor.RED).create();
    }

}
