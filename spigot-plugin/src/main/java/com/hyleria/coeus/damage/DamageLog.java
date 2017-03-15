package com.simplexitymc.kraken.damage;

import com.google.common.collect.Lists;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

/**
 * OutdatedVersion
 * At: 10:14 PM (May/07/2016)
 * cave-realms
 */

public class DamageLog
{

    private final Player relatingPlayer;
    private final ArrayList<DamageEvent> damageEvents;

    public DamageLog(Player relatingPlayer)
    {
        this.relatingPlayer = relatingPlayer;

        this.damageEvents = Lists.newArrayList();
    }

    public Player getFor()
    {
        return relatingPlayer;
    }

    public DamageLog addEvent(DamageEvent event)
    {
        damageEvents.add(event);
        return this;
    }

    public Collection<DamageEvent> fullLog()
    {
        return damageEvents;
    }

    public Collection<CombatEvent> combatLog()
    {
        Collection<CombatEvent> _events = new ArrayList<>();

        fullLog().stream().filter(event -> event instanceof CombatEvent).forEach(event -> _events.add(((CombatEvent) event)));

        return _events;
    }

}
