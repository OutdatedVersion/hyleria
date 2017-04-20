package com.hyleria.network;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hyleria.common.reference.Role;
import com.hyleria.util.Message;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/05/2017 (9:56 AM)
 */
@Singleton
public class PermissionManager
{

    /** when someone attempts to do something they don't have the {@link Role} to do they'll be sent this message */
    public static final String GEN_MISSING_PERM_MESSAGE = "Uh oh. You're missing the privilege to do that!";

    /** access player data */
    @Inject private AccountManager accountManager;

    /**
     * @param player the player
     * @param role the role to check for
     * @return whether they have the perms or not
     */
    public boolean has(Player player, Role role)
    {
        return has(player, role, GEN_MISSING_PERM_MESSAGE);
    }

    /**
     * @param player the player
     * @param role the role to check for
     * @param notify whether or not to send the player a message
     *               if they don't have the role provided
     * @return whether or not the player has the required perms or not
     */
    public boolean has(Player player, Role role, boolean notify)
    {
        return has(player, role, notify, GEN_MISSING_PERM_MESSAGE);
    }

    /**
     * @param player the player
     * @param role the role
     * @param permissionMessage the messages
     * @return whether they have the perms or not
     */
    public boolean has(Player player, Role role, String permissionMessage)
    {
        return has(player, role, true, permissionMessage);
    }

    /**
     * Checks whether the specified player has
     * the role (or something leveled hierarchy
     * higher). If they do not a missing permission
     * message is automatically sent to that person.
     *
     * @param player the player
     * @param role the required role
     * @param notify whether or not to tell the player they don't have permission
     * @param permissionMessage message to send if they don't have it
     * @return either yes or no
     */
    public boolean has(Player player, Role role, boolean notify, String permissionMessage)
    {
        if (accountManager.grab(player).role().ordinal() <= role.ordinal())
            return true;

        if (notify)
            Message.prefix("Commands").content(permissionMessage, ChatColor.RED).send(player);

        return false;
    }

}
