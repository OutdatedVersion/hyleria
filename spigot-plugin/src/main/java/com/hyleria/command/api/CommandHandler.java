package com.hyleria.command.api;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hyleria.Hyleria;
import com.hyleria.common.reference.Role;
import com.hyleria.network.PermissionManager;
import com.hyleria.util.Issues;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
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

    /** our plugin */
    @Inject private Hyleria hyleria;

    /** make sure people have the perms to execute stuff */
    @Inject private PermissionManager permissionManager;

    /** all of our commands */
    private Map<String, CommandInfo> commands = Maps.newHashMap();

    /** what we use to satisfy args */
    private Map<Class, ArgumentSatisfier> providers = Maps.newHashMap();

    /**
     * Looks over the classes in the
     * provided package, and if methods
     * exist in it annotated with our
     * command annotation, we'll process
     * those.
     *
     * @param pkg the fully qualified
     *            package name
     */
    public void registerInPackage(String pkg)
    {
        new FastClasspathScanner()
                .matchClassesWithMethodAnnotation(Command.class, (clazz, method) ->
                    registerCommands(hyleria.injector().getInstance(clazz))).scan();
    }

    /**
     * Look over the provided object
     * and register any commands in it
     * to our handler.
     *
     * @param object the object
     * @return this handler
     */
    public CommandHandler registerCommands(Object object)
    {
        // Iterate over a class looking for a method suitable
        // for being a used as a command. Keep in mind, only
        // public methods are eligible.
        for (Method method : object.getClass().getMethods())
        {
            if (method.isAnnotationPresent(Command.class))
            {
                final Command _ann = method.getAnnotation(Command.class);

                checkState(method.getParameterTypes()[0].equals(Player.class),
                           "The first argument in any command must be a Player (the one who executed the command)");

                final CommandInfo _info = new CommandInfo();

                _info.method = method;
                _info.executors = _ann.executor();
                _info.role = method.isAnnotationPresent(Permission.class)
                             ? method.getAnnotation(Permission.class).value()
                             : Role.PLAYER;

                _info.instanceOfPossessor = object;

                for (String executor : _info.executors)
                    commands.put(executor, _info);
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
                .ifPresent(info -> attemptCommandExecution(info.instanceOfPossessor, info.method, event.getPlayer(), info, _args));
    }

    public void attemptCommandExecution(Object object, Method method, Player player, CommandInfo info, String[] args)
    {
        // verify the player can actually execute this command
        if (info.role != Role.PLAYER)
            if (!permissionManager.has(player, info.role))
                return;


        // prepare parameters
        final Arguments _args = new Arguments(args);
        final Parameter[] _required = method.getParameters();
        Object[] _invokingWith = new Object[_required.length];

        // the player who ran the command is always the first parameter
        _invokingWith[0] = player;

        for (int i = 1; i < _required.length; i++)
        {
            final Parameter _working = _required[i];

            final ArgumentSatisfier _provider = providers.get(_working.getType());

            if (_provider != null)
                _invokingWith[i] = _provider.get(player, _args);
            else
                throw new IllegalArgumentException("Missing provider for parameter");
        }


        try
        {
            method.invoke(info.instanceOfPossessor, _invokingWith);
        }
        catch (Exception ex)
        {
            Issues.handle("Command Execution", ex);
        }
    }

    static class CommandInfo
    {
        Object instanceOfPossessor;
        Method method;

        String[] executors;
        Role role;
    }

}
