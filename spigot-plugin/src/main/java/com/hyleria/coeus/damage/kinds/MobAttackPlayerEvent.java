package com.simplexitymc.kraken.damage.kinds;

import com.simplexitymc.kraken.damage.DamageCause;
import com.simplexitymc.kraken.damage.DamageEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.apache.commons.lang.WordUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * OutdatedVersion
 * At: 5:30 PM (May/17/2016)
 * cave-realms
 */

public class MobAttackPlayerEvent extends DamageEvent
{

    private Entity dealtBy;

    public MobAttackPlayerEvent(Player victim, Entity dealtBy, double dealtDamage)
    {
        super(victim, dealtDamage, DamageCause.MOB);

        this.dealtBy = dealtBy;
    }

    @Override
    public BaseComponent[] getInformation()
    {
        return new ComponentBuilder("").append(WordUtils.capitalizeFully(dealtBy.getType().name().toLowerCase().replaceAll("_", " "))).color(ChatColor.AQUA)
                .append(" - ").color(ChatColor.GRAY).append(String.valueOf(getDamageDealt())).color(ChatColor.RED).create();
    }

}
