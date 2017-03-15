package com.hyleria.coeus.damage.kinds;

import com.hyleria.coeus.damage.CombatEvent;
import com.hyleria.coeus.damage.DamageCause;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;

/**
 * Damage dealt between multiple
 * people w/o a weapon.
 *
 * @author Ben (OutdatedVersion)
 * @since 11:12 PM (May/07/2016)
 */
public class DisarmedCombatEvent extends CombatEvent
{

    public DisarmedCombatEvent(Player victim, Player attacker, double dealtDamage)
    {
        super(victim, attacker, dealtDamage, DamageCause.FISTS);
    }

    @Override
    public BaseComponent[] information()
    {
        return new ComponentBuilder("Hand Combat - ").color(ChatColor.GRAY).append(String.valueOf(damageDealt()))
                   .color(ChatColor.RED).append(" damage").color(ChatColor.GRAY).create();
    }

}
