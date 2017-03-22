package com.hyleria.command.api;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hyleria.Hyleria;
import com.hyleria.common.inject.StartParallel;
import com.hyleria.common.reference.Role;
import com.hyleria.network.PermissionManager;
import com.hyleria.util.Colors;
import com.hyleria.util.Issues;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

import static com.google.common.base.Preconditions.checkState;

/**
 * @author Ben (OutdatedVersion)
 * @since Feb/28/2017 (5:50 PM)
 */
@Singleton
@StartParallel
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
     * @param clazz the class we're providing
     * @param satisfier logic for the provider
     * @param <T> type of provider
     * @return this handler
     */
    public <T> CommandHandler addProvider(Class<T> clazz, ArgumentSatisfier<T> satisfier)
    {
        providers.put(clazz, satisfier);
        return this;
    }

    /**
     * @param clazz the type we're providing
     * @param satisfierClass class of some satisifier
     * @return this handler
     */
    public <T> CommandHandler addProvider(Class<T> clazz, Class<? extends ArgumentSatisfier> satisfierClass)
    {
        providers.put(clazz, hyleria.injector().getInstance(satisfierClass));
        return this;
    }

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
        new FastClasspathScanner(pkg)
                .matchClassesWithMethodAnnotation(Command.class, (clazz, method) ->
                    registerCommands(hyleria.injector().getInstance(clazz))).scan();
    }

    /**
     * Register all of the provided classes
     * to our handler
     *
     * @param classes the classes
     * @return this handler
     */
    public CommandHandler registerCommands(Class<?>... classes)
    {
        for (Class clazz : classes)
            registerCommands(hyleria.injector().getInstance(clazz));

        return this;
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

                _info.possessor = object;

                for (String executor : _info.executors)
                    commands.put(executor.toLowerCase(), _info);
            }
        }

        return this;
    }

    @EventHandler ( priority = EventPriority.MONITOR, ignoreCancelled = true )
    public void interceptCommands(PlayerCommandPreprocessEvent event)
    {
        final String[] _split = event.getMessage().split(" ");
        final String _command = _split[0].substring(1).toLowerCase();

        final CommandInfo _info = commands.get(_command);

        if (_info != null)
        {
            final String[] _args = new String[_split.length - 1];
            System.arraycopy(_split, 1, _args, 0, _args.length);

            attemptCommandExecution(_info, _info.method, event.getPlayer(), _args);
        }
        else
        {
            event.getPlayer().sendMessage(Colors.bold(ChatColor.GRAY) + "We don't have anything matching that command for ya.");
        }

        event.setCancelled(true);
    }

    /**
     * @param info info
     * @param method the method
     * @param player the player
     * @param rawArguments what the player typed
     */
    private void attemptCommandExecution(CommandInfo info, Method method, Player player, String[] rawArguments)
    {
        // verify the player can actually execute this command
        if (info.role != Role.PLAYER)
            if (!permissionManager.has(player, info.role))
                return;


        // prepare parameters
        final Arguments _args = new Arguments(rawArguments);
        final Parameter[] _required = method.getParameters();
        Object[] _invokingWith = new Object[_required.length];

        // the player who ran the command is always the first parameter
        _invokingWith[0] = player;

        // let's start satisfying each parameter
        for (int i = 1; i < _required.length; i++)
        {
            final Parameter _working = _required[i];

            ArgumentSatisfier _provider;

            // if there's an annotation present we'll handle
            // it based on the recommendation of that provider
            // instead of the type of the parameter
            if (_working.getDeclaredAnnotations().length > 0)
            {
                // TODO(Ben): allow for more versatile annotations. so they can do different things based on the type of the parameter
                // only one deciding annotation allowed by parameter
                _provider = providers.get(_working.getDeclaredAnnotations()[0].annotationType());
            }
            else
            {
                _provider = providers.get(_working.getType());
            }

            // we should always have some sort of provider available
            if (_provider != null)
            {
                _invokingWith[i] = _provider.get(player, _args);

                if (_invokingWith[i] == null)
                {
                    if (_provider.fail() != null)
                        player.sendMessage(_provider.fail());

                    return;
                }
            }
            // in the case there isn't a provider, let's just try providing a String
            else if (_working.getType().isAssignableFrom(String.class))
            {
                _invokingWith[i] = _args.next();
            }
            // nothing we can do now, fail
            else throw new IllegalArgumentException("Missing provider for parameter type: " + _working.getType().getName());
        }


        try
        {
            // we've figured out our parameters; execute the command now
            method.invoke(info.possessor, _invokingWith);
        }
        catch (Exception ex)
        {
            Issues.handle("Command Execution", ex);
        }
    }


    /** data for a command */
    static class CommandInfo
    {
        /** an instance of where command method resides */
        Object possessor;

        /** the method for this command */
        Method method;

        /** what someone may type to run this command */
        String[] executors;

        /** the role someone must have to run this command, default: player */
        Role role;
    }

}
