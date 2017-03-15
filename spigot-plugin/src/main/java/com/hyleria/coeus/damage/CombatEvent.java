package com.simplexitymc.kraken.damage;

import org.bukkit.entity.Player;

/**
 * OutdatedVersion
 * At: 10:50 PM (May/07/2016)
 * cave-realms
 */

public abstract class CombatEvent extends DamageEvent
{
    private final Player attacker;

    public CombatEvent(Player victim, Player attacker, double dealtDamage, DamageCause damageCause)
    {
        super(victim, dealtDamage, damageCause);

        this.attacker = attacker;
    }

    public Player getAttacker()
    {
        return attacker;
    }
}
