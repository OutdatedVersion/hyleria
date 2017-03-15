package com.hyleria.coeus.damage.kinds;

import com.hyleria.coeus.damage.DamageCause;
import com.hyleria.coeus.damage.DamageEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;

/**
 * Damage dealt by lava
 *
 * @author Ben (OutdatedVersion)
 * @since 10:41 PM (May/07/2016)
 */
public class LavaDamageEvent extends DamageEvent
{

    public LavaDamageEvent(Player victim, double dealtDamage)
    {
        super(victim, dealtDamage, DamageCause.ENVIRONMENT);
    }

    @Override
    public BaseComponent[] information()
    {
        return new ComponentBuilder("").append("Lava - ").color(ChatColor.GRAY)
                  .append(String.valueOf(damageDealt())).color(ChatColor.RED).create();
    }

}
