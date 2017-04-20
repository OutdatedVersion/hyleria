package com.hyleria.command;

import com.google.inject.Inject;
import com.hyleria.command.api.Command;
import com.hyleria.command.api.annotation.Necessary;
import com.hyleria.command.api.annotation.Permission;
import com.hyleria.common.account.Account;
import com.hyleria.common.mongo.Database;
import com.hyleria.common.reference.Role;
import com.hyleria.network.AccountManager;
import com.hyleria.network.event.PlayerRoleUpdateEvent;
import com.hyleria.util.Message;
import com.hyleria.util.TextUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import static com.hyleria.util.Colors.PLAYER;
import static com.hyleria.util.Colors.bold;
import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.YELLOW;

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

    @Command ( executor = { "role", "rank" } )
    @Permission ( Role.ADMIN )
    public void run(Player player, @Necessary ( "Specify an online player's name" ) Player target,
                                   @Necessary ( "Provide the role you'd like to give!" ) Role role)
    {
        final Account _account = accountManager.grab(target);
        final Role _previous = _account.role();

        // prevent unnecessary database calls
        if (_previous == role)
        {
            Message.prefix("Account").content(_account.username(), ChatColor.GREEN)
                                     .content("is already")
                                     .content(role.name, ChatColor.YELLOW)
                                     .send(player);
            return;
        }

        _account.role(role, database);

        // notify hooks
        new PlayerRoleUpdateEvent(target, _previous, role).call();

        player.sendMessage(bold(GRAY) + "Updated " + bold(PLAYER) + TextUtil.s(target.getName()) + bold(GRAY) + " role to " + bold(YELLOW) + role.name);
        target.sendMessage(bold(GRAY) + "Your role has been updated to " + bold(YELLOW) + role.name);
    }

}
