package com.hyleria.util;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/15/2017 (12:08 AM)
 */
public class MessageUtil
{

    /**
     * Sends the provided message to
     * every online player.
     *
     * @param message the message
     */
    public static void everyone(final String message)
    {
        PlayerUtil.everyone().forEach(player -> player.sendMessage(message));
    }

}
