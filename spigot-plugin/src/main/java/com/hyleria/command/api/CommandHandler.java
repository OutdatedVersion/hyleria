package com.hyleria.command.api;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hyleria.Hyleria;
import com.hyleria.command.api.annotation.Necessary;
import com.hyleria.command.api.annotation.Permission;
import com.hyleria.command.api.satisfier.ExecutedBySatisfier;
import com.hyleria.command.api.satisfier.PlayerSatisfier;
import com.hyleria.command.api.satisfier.RoleSatisfier;
import com.hyleria.common.reference.Role;
import com.hyleria.network.PermissionManager;
import com.hyleria.util.Issues;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkState;

/**
 * @author Ben (OutdatedVersion)
 * @since Feb/28/2017 (5:50 PM)
 */
@Singleton
public class CommandHandler implements Listener
{

    /** the message sent when someone runs a command that doesn't exist */
    private static final BaseComponent[] HELP_MESSAGE = new ComponentBuilder("Unknown command.. we're here to ")
            .color(net.md_5.bungee.api.ChatColor.GRAY).bold(true).append("/help").color(net.md_5.bungee.api.ChatColor.YELLOW)
            .append(" you.").color(net.md_5.bungee.api.ChatColor.GRAY).create();

    /** all the providers we need */
    public static final Collection<Class<? extends ArgumentSatisfier>> DEFAULT_PROVIDERS = Lists.newArrayList(
            ExecutedBySatisfier.class, RoleSatisfier.class, PlayerSatisfier.class
    );

    /** default commands that we won't let players run unless we have one that overrides it */
    public static final Set<String> BLOCKED_COMMANDS = Sets.newHashSet(
            "help", "pl", "plugins", "ver", "version", "icanhasbukkit", "about"
    );

    /** our plugin */
    @Inject private Hyleria hyleria;

    /** make sure people have the perms to execute stuff */
    @Inject private PermissionManager permissionManager;

    /** all of our commands */
    private Map<String, BaseCommandInfo> commands = Maps.newHashMap();

    /** what we use to satisfy args */
    private Map<Class, ArgumentSatisfier> providers = Maps.newHashMap();

    /**
     * @param satisfiers a collection of providers to register
     * @return this handler
     */
    public CommandHandler addProviders(Collection<Class<? extends ArgumentSatisfier>> satisfiers)
    {
        satisfiers.forEach(this::addProvider);
        return this;
    }

    /**
     * @param satisfier logic for the provider
     * @param <T> type of provider
     * @return this handler
     */
    public <T> CommandHandler addProvider(ArgumentSatisfier<T> satisfier)
    {
        providers.put(satisfier.satisfies(), satisfier);
        return this;
    }

    /**
     * @param satisfierClass class of some satisifier
     * @return this handler
     */
    public <T> CommandHandler addProvider(Class<? extends ArgumentSatisfier> satisfierClass)
    {
        addProvider(hyleria.get(satisfierClass));
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
                    registerCommandsFromObject(hyleria.injector().getInstance(clazz))).scan();
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
            registerCommandsFromObject(hyleria.get(clazz));

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
    public CommandHandler registerCommandsFromObject(Object object)
    {
        // Iterate over a class looking for a method suitable
        // for being a used as a command. Keep in mind, only
        // public methods are eligible.
        for (Method method : Stream.of(object.getClass().getMethods()).sorted((one, two) -> one.isAnnotationPresent(SubCommand.class) ? 1 : 0).collect(Collectors.toList()))
        {
            if (method.isAnnotationPresent(Command.class))
            {
                final Command _ann = method.getAnnotation(Command.class);

                checkState(method.getParameterTypes()[0].equals(Player.class),
                           "The first argument in any command must be a Player (the one who executed the command)");

                final BaseCommandInfo _info = new BaseCommandInfo();

                _info.method = method;
                _info.executors = Sets.newHashSet(_ann.executor());
                _info.role = roleFromMethod(method);

                _info.possessor = object;

                for (String executor : _info.executors)
                    commands.put(executor.toLowerCase(), _info);
            }

            if (method.isAnnotationPresent(SubCommand.class))
            {
                final SubCommand _ann = method.getAnnotation(SubCommand.class);

                checkState(method.getParameterTypes()[0].equals(Player.class), "Missing player arg in pos 1 (0)");
                checkState(commands.containsKey(_ann.of()), "Parent command must be registered first!");

                final SubCommandInfo _info = new SubCommandInfo();

                _info.executors = Sets.newHashSet(_ann.executors());
                _info.method = method;
                _info.role = roleFromMethod(method);
                _info.possessor = object;

                commands.get(_ann.of()).addSubCommand(_info);
            }
        }

        return this;
    }

    @EventHandler ( priority = EventPriority.MONITOR, ignoreCancelled = true )
    public void interceptCommands(PlayerCommandPreprocessEvent event)
    {
        final String[] _split = event.getMessage().split(" ");
        final String _command = _split[0].substring(1).toLowerCase();

        final BaseCommandInfo _info = commands.get(_command);

        if (_info != null)
        {
            final String[] _args = new String[_split.length - 1];
            System.arraycopy(_split, 1, _args, 0, _args.length);

            // these are my least favorite things to do
            final CommandInfo[] _invokeInfo = { _info };

            if (_args.length >= 1 && _info.subCommands != null)
            {
                _info.subCommands.stream().filter(s -> s.executors.contains(_args[0])).findFirst().ifPresent(s -> _invokeInfo[0] = s);
            }

            attemptCommandExecution(_invokeInfo[0], _invokeInfo[0].method, event.getPlayer(), _invokeInfo[0] instanceof SubCommandInfo ? Arrays.copyOfRange(_args, 1, _args.length)
                                                                                                                                       : _args);

            event.setCancelled(true);
            return;
        }
        else if (BLOCKED_COMMANDS.contains(_command))
        {
            event.getPlayer().spigot().sendMessage(HELP_MESSAGE);
            event.setCancelled(true);
        }
        else if (!permissionManager.has(event.getPlayer(), Role.ADMIN) && !event.getPlayer().isOp())
        {
            event.getPlayer().spigot().sendMessage(HELP_MESSAGE);
            event.setCancelled(true);
        }


        if (!event.isCancelled() && hyleria.getServer().getHelpMap().getHelpTopic("/" + _command) == null)
        {
            event.getPlayer().spigot().sendMessage(HELP_MESSAGE);
            event.setCancelled(true);
        }
    }

    /**
     * @param info info
     * @param method the method
     * @param player the player
     * @param rawArguments what the player typed
     */
    private void attemptCommandExecution(CommandInfo info, Method method, Player player, String[] rawArguments)
    {
        try
        {
            // verify the player can actually execute this command
            if (info.role != Role.PLAYER)
                if (!permissionManager.has(player, info.role))
                    return;

            // prepare parameters
            final Arguments _args = new Arguments(rawArguments);
            final Parameter[] _required = method.getParameters();
            final Object[] _invokingWith = new Object[_required.length];

            // the player who ran the command is always the first parameter
            _invokingWith[0] = player;

            // let's start satisfying each parameter
            for (int i = 1; i < _required.length; i++)
            {
                final Parameter _working = _required[i];

                ArgumentSatisfier _provider;

                // make sure if we need something & it doesn't exist that we fail
                final Necessary _necessary = _working.getDeclaredAnnotation(Necessary.class);

                if (_necessary != null && _args.currentPosition() < i)
                {
                    player.sendMessage(_necessary.value());
                    return;
                }

                // if there's an annotation present we'll handle
                // it based on the recommendation of that provider
                // instead of the type of the parameter
                if (_working.getDeclaredAnnotations().length > 0)
                {
                    // TODO(Ben): allow for more versatile annotations. so they can do different things based on the type of the parameter
                    // only one deciding annotation allowed by parameter
                    // this does not include the "necessary" annotation

                    _provider = providers.get(_working.getDeclaredAnnotations()[0].annotationType());
                }
                else
                {
                    _provider = providers.get(_working.getType());
                }

                // we should always have some sort of provider available
                if (_provider != null)
                {
                    final Arguments _copy = _args.clone();

                    _invokingWith[i] = _provider.get(player, _args);

                    if (_invokingWith[i] == null)
                    {
                        final String _next = _copy.next();

                        // TODO(Ben): this probably shouldn't be invoked twice; find a way around this
                        if (_provider.fail(_next) != null)
                            player.sendMessage(_provider.fail(_next));

                        return;
                    }
                }
                // in the case there isn't a provider, let's just try providing a String
                else if (_working.getType().equals(String.class))
                {
                    _invokingWith[i] = _args.next();
                }
                // nothing we can do now, fail
                else throw new IllegalArgumentException("Missing provider for parameter type: " + _working.getType().getName());
            }


            // we've figured out our parameters; execute the command now
            method.invoke(info.possessor, _invokingWith);
        }
        catch (Exception ex)
        {
            Issues.handle("Command Execution", ex);
        }
    }

    /**
     * @param method the method
     * @return the role for permission tasks
     */
    private static Role roleFromMethod(Method method)
    {
        return method.isAnnotationPresent(Permission.class)
                ? method.getAnnotation(Permission.class).value()
                : Role.PLAYER;
    }

    /**
     * Send's our "unknown command" message to
     * the provided player
     *
     * @param player the target player
     */
    public static void sendHelpMessage(Player player)
    {
        player.spigot().sendMessage(HELP_MESSAGE);
    }

    static abstract class CommandInfo
    {
        /** an instance of where command method resides */
        Object possessor;

        /** the method for this command */
        Method method;

        /** what someone may type to run this command */
        Set<String> executors;

        /** the role someone must have to run this command, default: player */
        Role role;
    }

    /** data for a command */
    static class BaseCommandInfo extends CommandInfo
    {
        /** the sub-commands for this command */
        Set<SubCommandInfo> subCommands;

        /**
         * @param info the sub-command to add
         */
        void addSubCommand(SubCommandInfo info)
        {
            if (subCommands == null)
                subCommands = Sets.newHashSet();

            subCommands.add(info);
        }
    }

    /** data for a sub-commands of a {@link BaseCommandInfo} */
    static class SubCommandInfo extends CommandInfo { }

}
