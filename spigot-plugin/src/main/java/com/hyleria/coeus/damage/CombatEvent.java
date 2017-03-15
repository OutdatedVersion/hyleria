package com.hyleria.coeus.damage;

import org.bukkit.entity.Player;

/**
 * Something combat related
 * occurred (PvP)
 *
 * @author Ben (OutdatedVersion)
 * @since 10:50 PM (May/07/2016)
 */
public abstract class CombatEvent extends DamageEvent
{

    /** who started this attack */
    private final Player attacker;

    public CombatEvent(Player victim, Player attacker, double dealtDamage, DamageCause damageCause)
    {
        super(victim, dealtDamage, damageCause);

        this.attacker = attacker;
    }

    /**
     * @return the person who started this
     */
    public Player attacker()
    {
        return attacker;
    }

}
