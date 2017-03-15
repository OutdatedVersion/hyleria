package com.simplexitymc.kraken.damage;

/**
 * OutdatedVersion
 * At: 10:11 PM (May/07/2016)
 * cave-realms
 */

public enum DamageCause
{
    RANGED("Ranged Combat"),
    FISTS("Hand Combat"),
    WEAPON(""),
    MOB("Creature Damage"),
    ENVIRONMENT("Environment Damage");

    private final String displayName;

    DamageCause(String name)
    {
        this.displayName = name;
    }
}
