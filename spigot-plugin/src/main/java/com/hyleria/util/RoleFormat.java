package com.hyleria.util;

import com.google.common.collect.Maps;
import com.hyleria.common.reference.Role;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.Map;

/**
 * @author Ben (OutdatedVersion)
 * @since Feb/24/2017 (3:14 PM)
 */
public class RoleFormat
{

    /** a map containing the roles to the colors */
    private static final Map<Role, ChatColor> COLOR_RELATION = Maps.newHashMap();

    static
    {
        // population
        Arrays.stream(Role.values()).forEach(role -> COLOR_RELATION.put(role, ChatColor.getByChar(role.colorCode)));
    }

    /**
     * @param role what we're looking for
     * @return the {@link ChatColor} of the role
     */
    public static ChatColor colorFor(Role role)
    {
        return COLOR_RELATION.get(role);
    }

    /**
     * @param role the role
     * @return what this role looks like in chat
     */
    public static String chatFormat(Role role)
    {
        return role == Role.PLAYER ? ChatColor.GRAY.toString() : (colorFor(role) + role.toNameColorless());
    }

    /**
     *
     * @param data
     * @return
     */
    public static String chatFormatFromData(String data)
    {
        final String[] _split = data.split(":");
        return ChatColor.valueOf(_split[0]) + "[" + _split[1] + "]";
    }

}
