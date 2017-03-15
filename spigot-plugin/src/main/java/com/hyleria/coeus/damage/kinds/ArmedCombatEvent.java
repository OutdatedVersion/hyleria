package com.simplexitymc.kraken.damage.kinds;

import com.simplexitymc.kraken.damage.CombatEvent;
import com.simplexitymc.kraken.damage.DamageCause;
import com.simplexitymc.util.WordUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * OutdatedVersion
 * At: 10:26 PM (May/07/2016)
 * cave-realms
 */

public class ArmedCombatEvent extends CombatEvent
{

    private final ItemStack weapon;

    public ArmedCombatEvent(Player victim, Player attacker, double dealtDamage, ItemStack weapon)
    {
        super(victim, attacker, dealtDamage, DamageCause.WEAPON);

        this.weapon = weapon;
    }

    public ItemStack getWeapon()
    {
        return weapon;
    }

    @Override
    public BaseComponent[] getInformation()
    {
        ComponentBuilder _hoverText = new ComponentBuilder("");
        Map<Enchantment, Integer> _enchants = weapon.getEnchantments();

        if (_enchants == null || _enchants.size() == 0)
            _hoverText.append("Vanilla Weapon").color(ChatColor.GRAY);
        else
            _enchants.forEach((enchant, level) -> _hoverText.append(enchant.getName()).color(ChatColor.GREEN).append(" (Level: " + level + ")").color(ChatColor.GRAY));

        return new ComponentBuilder("Weapon Combat - ").color(ChatColor.GRAY).append(WordUtil.formatItemName(weapon, true))
                                                       .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, _hoverText.create()))
                                                       .append(String.valueOf(getDamageDealt())).color(ChatColor.RED).append(" damage").color(ChatColor.GRAY).create();
    }

}


