package com.hyleria.command;

import com.google.inject.Inject;
import com.hyleria.command.api.Command;
import com.hyleria.command.api.Permission;
import com.hyleria.common.mongo.Database;
import com.hyleria.common.reference.Role;
import com.hyleria.network.AccountManager;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.DARK_GREEN;
import static org.bukkit.Color.GREEN;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/19/2017 (10:18 AM)
 */
public class UpdateRoleCommand
{

    // TODO(Ben): add cross-server support via redis

    /** persistent database instance */
    @Inject private Database database;

    /** access to player accounts */
    @Inject private AccountManager accountManager;

    @Command ( executor = "role" )
    @Permission ( Role.ADMIN )
    public void run(Player player, Player target, Role role)
    {
        accountManager.grab(target).role(role, database);

        player.sendMessage(GREEN + "Updated ");
        target.sendMessage(GREEN + "Your role has been updated to " + DARK_GREEN + role.name);
    }

}
