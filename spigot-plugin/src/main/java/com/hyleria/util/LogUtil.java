package com.hyleria.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * @author Ben (OutdatedVersion)
 * @since Feb/24/2017 (3:25 PM)
 */
public class LogUtil
{

    /**
     * @param message [General] {@code message}
     */
    public static void info(String message)
    {
        info("General", message);
    }

    /**
     * @param message [General] {@code message}
     */
    public static void info(String header, String message)
    {
        log(header, message, ChatColor.GREEN, ChatColor.WHITE);
    }

    /**
     * @param message [Warning] {@code message}
     */
    public static void warn(String message)
    {
        warn("Warning", message);
    }

    /**
     * @param message [Warning] {@code message}
     */
    public static void warn(String header, String message)
    {
        log(header, message, ChatColor.YELLOW, ChatColor.WHITE);
    }

    /**
     * @param message [Severe] {@code message}
     */
    public static void severe(String message)
    {
        severe("Severe", message);
    }

    /**
     * @param message [Severe] {@code message}
     */
    public static void severe(String header, String message)
    {
        log(header, message, ChatColor.RED, ChatColor.WHITE);
    }

    /**
     * @param message [System] {@code message}
     */
    public static void system(String message)
    {
        system("System", message);
    }

    /**
     * @param message [System] {@code message}
     */
    public static void system(String header, String message)
    {
        log(header, message, ChatColor.DARK_GRAY, ChatColor.GRAY);
    }

    /**
     * @param message [Unknown] {@code message}
     */
    public static void log(String header, String message, ChatColor headerColor)
    {
        log(header, message, headerColor, ChatColor.WHITE);
    }

    /**
     * @param header [header]
     * @param message after header
     * @param headerColor color of header
     * @param messageColor color of message
     */
    public static void log(String header, String message, ChatColor headerColor, ChatColor messageColor)
    {
        Bukkit.getConsoleSender().sendMessage(String.format(headerColor + "[%s] %s", header, messageColor + message));
    }

}
