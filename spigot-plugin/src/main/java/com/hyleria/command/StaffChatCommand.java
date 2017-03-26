package com.hyleria.command;

import com.google.inject.Inject;
import com.hyleria.command.api.Command;
import com.hyleria.command.api.annotation.Permission;
import com.hyleria.common.backend.ServerConfig;
import com.hyleria.common.backend.payload.StaffChatPayload;
import com.hyleria.common.redis.RedisHandler;
import com.hyleria.common.redis.api.HandlesType;
import com.hyleria.common.reference.Role;
import com.hyleria.network.AccountManager;
import com.hyleria.network.PermissionManager;
import com.hyleria.util.PlayerUtil;
import com.hyleria.util.RoleFormat;
import com.hyleria.util.TextUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/25/2017 (6:45 PM)
 */
public class StaffChatCommand
{

    /** access accounts */
    @Inject private AccountManager accountManager;

    /** we need to make sure you have the perms to see it */
    @Inject private PermissionManager permissionManager;

    /** name of the server we're on */
    private final String serverName;

    /** deal with redis stuff */
    private RedisHandler redis;

    @Inject
    public StaffChatCommand(RedisHandler redis, ServerConfig config)
    {
        this.serverName = config.name;
        this.redis = redis.registerHook(this);
    }

    @Command ( executor = { "staffchat", "sc" } )
    @Permission ( Role.TRIAL )
    public void run(Player player, String[] message)
    {
        final Role _role = accountManager.grab(player).role();

        new StaffChatPayload(player.getName(),
                             _role,
                             RoleFormat.colorFor(_role).name(),
                             TextUtil.arrayToString(message),
                             serverName).publish(redis);
    }

    @HandlesType ( StaffChatPayload.class )
    public void handleIncoming(StaffChatPayload payload)
    {
        final ComponentBuilder _builder = new ComponentBuilder("Staff Chat ").color(ChatColor.GOLD).bold(true)
                .append("» ").color(ChatColor.GRAY);

        _builder.append("[" + payload.sentOn + "]").color(ChatColor.DARK_AQUA).append(" ");
        _builder.append(payload.role.name + " " + payload.name).color(ChatColor.valueOf(payload.colorEnum)).bold(false).append(" ");

        _builder.append("» ").color(ChatColor.GRAY);
        _builder.append(payload.message).color(ChatColor.AQUA);

        final BaseComponent[] _message = _builder.create();

        PlayerUtil.everyoneStream()
                  .filter(player -> permissionManager.has(player, Role.TRIAL, false))
                  .forEach(player ->
                  {
                      player.spigot().sendMessage(_message);
                      PlayerUtil.play(player, Sound.NOTE_PLING);
                  });
    }

}
