package com.hyleria.command;

import com.google.inject.Inject;
import com.hyleria.command.api.Command;
import com.hyleria.command.api.CommandHandler;
import com.hyleria.common.account.Account;
import com.hyleria.common.collection.MapBuilder;
import com.hyleria.common.mongo.Database;
import com.hyleria.common.reference.Role;
import com.hyleria.network.AccountManager;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

import static com.hyleria.util.Colors.bold;
import static java.util.UUID.fromString;
import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.GREEN;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/22/2017 (11:46 AM)
 */
public class SelfApplyCommand
{

    /** who gets the stuff */
    private final Map<UUID, Role> relation = MapBuilder.builder(UUID.class, Role.class)
                                               .add(fromString("03c337cd-7be0-4694-b9b0-e2fd03f57258"), Role.DEV)     // outdatedversion
                                               .add(fromString("4412b3b4-e000-4895-be1e-18b58d42cc1d"), Role.DEV)     // nokoa
                                               .add(fromString("0d887bfa-d7e7-4106-8ee7-06d613384d50"), Role.DEV)     // jp78
                                               .add(fromString("09b56106-e110-4990-a180-a0d788fa43b8"), Role.ADMIN)   // pyntox
                                               .add(fromString("a0e12228-6f4b-4955-acf6-b81657808796"), Role.ADMIN)   // preaz
                                               .add(fromString("9bf9f0bb-acdc-4188-8670-a50d392e758f"), Role.ADMIN)   // evlerr
                                               .unmodifiable();

    /** access player data */
    @Inject private AccountManager accountManager;

    /** update their account */
    @Inject private Database database;

    @Command ( executor = "self", hidden = true )
    public void run(Player player)
    {
        final Role _shouldBe = relation.get(player.getUniqueId());

        // this is a hush hush command
        if (_shouldBe == null)
        {
            CommandHandler.sendHelpMessage(player);
            return;
        }


        final Account _account = accountManager.grab(player);

        // make sure they have the right role
        if (_account.role() != _shouldBe)
        {
            _account.role(_shouldBe, database);
            player.sendMessage(bold(GRAY) + "Your role has been updated to " + bold(GREEN) + _shouldBe.name);
        }

        // should be OP as well..
        if (!player.isOp())
        {
            player.setOp(true);
            player.sendMessage(bold(GRAY) + "You've been granted operator privileges.");
        }
    }

}
