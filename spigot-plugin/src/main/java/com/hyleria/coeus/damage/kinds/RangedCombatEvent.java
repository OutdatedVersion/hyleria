package com.simplexitymc.kraken.damage.kinds;

import com.simplexitymc.kraken.damage.CombatEvent;
import com.simplexitymc.kraken.damage.DamageCause;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * OutdatedVersion
 * At: 10:15 PM (May/07/2016)
 * cave-realms
 */

public class RangedCombatEvent extends CombatEvent
{

    private final int blocksAway;
    private final Map<Enchantment, Integer> bowEnchantments;

    public RangedCombatEvent(Player victim, Player attacker, double dealtDamage, int blocksAway, Map<Enchantment, Integer> bowEnchants)
    {
        super(victim, attacker, dealtDamage, DamageCause.RANGED);

        this.blocksAway = blocksAway;
        this.bowEnchantments = bowEnchants;
    }

    public int getBlocksAway()
    {
        return blocksAway;
    }

    public Map<Enchantment, Integer> getBowEnchantments()
    {
        return bowEnchantments;
    }

    @Override
    public BaseComponent[] getInformation()
    {
        return new ComponentBuilder("Ranged Combat - ").color(ChatColor.GRAY).append(String.valueOf(blocksAway))
                                                       .color(ChatColor.RED).append(" block away").color(ChatColor.GRAY).create();
    }

}
