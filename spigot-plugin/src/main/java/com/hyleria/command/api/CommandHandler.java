package com.hyleria.command.api;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hyleria.Hyleria;
import com.hyleria.commons.reference.Role;
import com.hyleria.network.PermissionManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;

import static com.google.common.base.Preconditions.checkState;

/**
 * @author Ben (OutdatedVersion)
 * @since Feb/28/2017 (5:50 PM)
 */
@Singleton
public class CommandHandler
{

    @Inject
    private Hyleria hyleria;

    @Inject
    private PermissionManager permissionManager;

    private Map<String, CommandInfo> commands;

    public CommandHandler registerCommands(Object object)
    {
        if (commands == null)
            commands = Maps.newHashMap();

        // Iterate over a class looking for a method suitable
        // for being a used as a command. Keep in mind, only
        // public methods are eligible.
        for (Method method : object.getClass().getMethods())
        {
            if (method.isAnnotationPresent(Command.class))
            {
                final Command _ann = method.getAnnotation(Command.class);
                final Parameter[] _parameters = method.getParameters();
                final CommandInfo _info = new CommandInfo();

                checkState(_parameters[0].getType().equals(Player.class), "The first argument in any command must be a Player");
            }
        }

        return this;
    }

    @EventHandler ( priority = EventPriority.MONITOR, ignoreCancelled = true )
    public void interceptCommands(PlayerCommandPreprocessEvent event)
    {
        final String[] _split = event.getMessage().split(" ");
        final String _command = _split[0].substring(1).toLowerCase();
        final String[] _args = new String[_split.length - 1];

        System.arraycopy(_split, 1, _args, 0, _args.length);

        commands.values()
                .stream()
                .filter(data -> Arrays.stream(data.executors).anyMatch(_command::equals))
                .findFirst()
                .ifPresent(info ->
                {

                });
    }

    public void attemptCommandExecution(Object object, Method method, Player player, CommandInfo info)
    {
        // permissions
        if (method.isAnnotationPresent(Permission.class))
        {
            final Role _role = method.getAnnotation(Permission.class).value();


        }

        // invoke
    }

    static class CommandInfo
    {
        Method method;

        String[] executors;
        Role role;

        String node()
        {
            return "command." + executors[0].toLowerCase();
        }
    }

}
