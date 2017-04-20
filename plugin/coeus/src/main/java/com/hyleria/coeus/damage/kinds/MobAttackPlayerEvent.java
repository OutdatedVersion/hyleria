package com.hyleria.coeus.damage.kinds;

import com.hyleria.coeus.damage.DamageCause;
import com.hyleria.coeus.damage.DamageEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.apache.commons.lang.WordUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * Whenever a mob attacks a player
 * this event will be fired off.
 *
 * @author Ben (OutdatedVersion)
 * @since 5:30 PM (May/17/2016)
 */
public class MobAttackPlayerEvent extends DamageEvent
{

    /** what attacked the player */
    private Entity dealtBy;

    public MobAttackPlayerEvent(Player victim, Entity dealtBy, double dealtDamage)
    {
        super(victim, dealtDamage, DamageCause.MOB);

        this.dealtBy = dealtBy;
    }

    @Override
    public BaseComponent[] information()
    {
        return new ComponentBuilder("").append(WordUtils.capitalizeFully(dealtBy.getType().name().toLowerCase().replaceAll("_", " "))).color(ChatColor.AQUA)
                .append(" - ").color(ChatColor.GRAY).append(String.valueOf(damageDealt())).color(ChatColor.RED).create();
    }

}
