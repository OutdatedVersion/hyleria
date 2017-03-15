package com.hyleria.coeus.damage.kinds;

import com.hyleria.coeus.damage.CombatEvent;
import com.hyleria.coeus.damage.DamageCause;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Whenever players engage in
 * combat using bows/other
 * ranged weapons we'll hit
 * this event.
 *
 * @author Ben (OutdatedVersion)
 * @since 10:15 PM (May/07/2016)
 */
public class RangedCombatEvent extends CombatEvent
{

    /** how far we were when this happened */
    private final int blocksAway;

    /** the enchantments on the bow */
    private final Map<Enchantment, Integer> bowEnchantments;

    public RangedCombatEvent(Player victim, Player attacker, double dealtDamage, int blocksAway, Map<Enchantment, Integer> bowEnchants)
    {
        super(victim, attacker, dealtDamage, DamageCause.RANGED);

        this.blocksAway = blocksAway;
        this.bowEnchantments = bowEnchants;
    }

    /**
     * @return the distance this was hit from (in blocks)
     */
    public int distance()
    {
        return blocksAway;
    }

    /**
     * @return the enchantments on the bow used
     */
    public Map<Enchantment, Integer> bowEnchants()
    {
        return bowEnchantments;
    }

    @Override
    public BaseComponent[] information()
    {
        return new ComponentBuilder("Ranged Combat - ").color(ChatColor.GRAY).append(String.valueOf(blocksAway))
                                                       .color(ChatColor.RED).append(" block away").color(ChatColor.GRAY).create();
    }

}
