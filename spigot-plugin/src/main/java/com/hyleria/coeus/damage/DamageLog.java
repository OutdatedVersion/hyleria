package com.hyleria.coeus.damage;

import com.google.common.collect.Lists;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A set of events on has happened
 * to a player over the course of
 * a match.
 *
 * @author Ben (OutdatedVersion)
 * @since 10:14 PM (May/07/2016)
 */
public class DamageLog
{

    /** the player this log is for */
    private final Player player;
    private final List<DamageEvent> damageEvents;

    public DamageLog(Player player)
    {
        this.player = player;
        this.damageEvents = Lists.newArrayList();
    }

    /**
     * @return the player this log is for
     */
    public Player player()
    {
        return player;
    }

    /**
     * Appends an event to this log
     *
     * @param event the event
     * @return this log
     */
    public DamageLog addEvent(DamageEvent event)
    {
        damageEvents.add(event);
        return this;
    }

    /**
     * @return all of the damage events from this log
     */
    public Collection<DamageEvent> fullLog()
    {
        return damageEvents;
    }

    /**
     * @return all of the combat events from this log
     */
    public Collection<CombatEvent> combatLog()
    {
        return damageEvents.stream().filter(event -> event instanceof CombatEvent)
                                    .map(event -> (CombatEvent) event)
                                    .collect(Collectors.toList());
    }

}
