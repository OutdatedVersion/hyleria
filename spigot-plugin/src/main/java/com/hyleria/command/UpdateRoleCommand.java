package com.hyleria.command;

import com.google.inject.Inject;
import com.hyleria.command.api.Command;
import com.hyleria.command.api.Permission;
import com.hyleria.common.account.Account;
import com.hyleria.common.mongo.Database;
import com.hyleria.common.reference.Role;
import com.hyleria.network.AccountManager;
import com.hyleria.network.event.PlayerRoleUpdateEvent;
import com.hyleria.util.TextUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static com.hyleria.util.Colors.PLAYER;
import static com.hyleria.util.Colors.bold;
import static org.bukkit.ChatColor.*;

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
        final Account _account = accountManager.grab(target);
        final Role _previous = _account.role();

        _account.role(role, database);

        // notify hooks
        new PlayerRoleUpdateEvent(target, _previous, role).call();

        player.sendMessage(GRAY + "Updated " + bold(PLAYER) + TextUtil.s(target.getName()) + bold(ChatColor.GRAY) + " role to " + bold(GREEN) + role.toNameColorless());
        target.sendMessage(GRAY + "Your role has been updated to " + DARK_GREEN + role.name);
    }

}
