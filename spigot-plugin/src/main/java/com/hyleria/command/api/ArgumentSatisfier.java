package com.hyleria.command.api;

import org.bukkit.entity.Player;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/19/2017 (10:03 AM)
 */
public interface ArgumentSatisfier<T>
{

    /**
     * Grabs something of type {@code T}
     * from the provided data
     *
     * @param player the player
     * @param args the args provided at execution
     * @return something of type T
     */
    T get(Player player, Arguments args);

    /**
     * @return the message to send when if
     *         we fail to do what {@link #get(Player, Arguments)}
     *         does
     */
    String fail();

}
