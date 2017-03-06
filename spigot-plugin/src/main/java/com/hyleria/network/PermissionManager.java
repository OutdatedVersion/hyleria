package com.hyleria.network;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hyleria.commons.reference.Role;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/05/2017 (9:56 AM)
 */
@Singleton
public class PermissionManager
{

    /** when someone attempts to do something they don't have the {@link Role} to do they'll be sent this message */
    public static final String GEN_MISSING_PERM_MESSAGE = ChatColor.RED + "" + ChatColor.BOLD +
                                                                    "Uh oh. You're missing the privileges to do that!";

    /** access player data */
    @Inject
    private AccountManager accountManager;

    public boolean has(Player player, Role role)
    {
        if (accountManager.grab(player).role().ordinal() <= role.ordinal())
            return true;

        player.sendMessage(GEN_MISSING_PERM_MESSAGE);

        return false;
    }

}
