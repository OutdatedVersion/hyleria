package com.hyleria.coeus.damage.kinds;

import com.hyleria.coeus.damage.CombatEvent;
import com.hyleria.coeus.damage.DamageCause;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * Damage dealt between multiple people.
 *
 * @author Ben (OutdatedVersion)
 * @since 10:26 PM (May/07/2016)
 */
public class ArmedCombatEvent extends CombatEvent
{

    /** the weapon used during the combat */
    private final ItemStack weapon;

    public ArmedCombatEvent(Player victim, Player attacker, double dealtDamage, ItemStack weapon)
    {
        super(victim, attacker, dealtDamage, DamageCause.WEAPON);

        this.weapon = weapon;
    }

    public ItemStack weapon()
    {
        return weapon;
    }

    @Override
    public BaseComponent[] information()
    {
        ComponentBuilder _hoverText = new ComponentBuilder("");
        Map<Enchantment, Integer> _enchants = weapon.getEnchantments();

        if (_enchants == null || _enchants.size() == 0)
            _hoverText.append("Vanilla Weapon").color(ChatColor.GRAY);
        else
            _enchants.forEach((enchant, level) -> _hoverText.append(enchant.getName()).color(ChatColor.GREEN).append(" (Level: " + level + ")").color(ChatColor.GRAY));

        // WordUtil.formatItemName(weapon, true)
        return new ComponentBuilder("Weapon Combat - ").color(ChatColor.GRAY).append("")
                                                       .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, _hoverText.create()))
                                                       .append(String.valueOf(damageDealt())).color(ChatColor.RED).append(" damage").color(ChatColor.GRAY).create();
    }

}


