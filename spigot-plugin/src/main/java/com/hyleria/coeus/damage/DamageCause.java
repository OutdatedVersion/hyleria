package com.hyleria.coeus.damage;

/**
 * Possible ways someone may
 * be injured
 *
 * @author Ben (OutdatedVersion)
 * @since 10:11 PM (May/07/2016)
 */
public enum DamageCause
{

    RANGED("Ranged Combat"),
    FISTS("Hand Combat"),
    WEAPON(""),
    MOB("Creature Damage"),
    ENVIRONMENT("Environment Damage");

    /** the public facing name */
    public final String name;

    DamageCause(String name)
    {
        this.name = name;
    }

}
