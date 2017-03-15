package com.simplexitymc.kraken.damage.kinds;

import com.simplexitymc.kraken.damage.CombatEvent;
import com.simplexitymc.kraken.damage.DamageCause;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;

/**
 * OutdatedVersion
 * At: 11:12 PM (May/07/2016)
 * cave-realms
 */

public class DisarmedCombatEvent extends CombatEvent
{

    public DisarmedCombatEvent(Player victim, Player attacker, double dealtDamage)
    {
        super(victim, attacker, dealtDamage, DamageCause.FISTS);
    }

    @Override
    public BaseComponent[] getInformation()
    {
        return new ComponentBuilder("Hand Combat - ").color(ChatColor.GRAY).append(String.valueOf(getDamageDealt()))
                   .color(ChatColor.RED).append(" damage").color(ChatColor.GRAY).create();
    }

}
