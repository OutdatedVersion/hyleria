package com.hyleria.module;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hyleria.Hyleria;
import com.hyleria.command.api.Command;
import com.hyleria.command.api.SubCommand;
import com.hyleria.command.api.annotation.Permission;
import com.hyleria.common.backend.ServerConfig;
import com.hyleria.common.inject.StartParallel;
import com.hyleria.common.redis.RedisHandler;
import com.hyleria.common.reference.Role;
import com.hyleria.common.time.Time;
import com.hyleria.util.Issues;
import com.hyleria.util.Message;
import com.hyleria.util.Scheduler;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_7_R4.MinecraftServer;
import net.minecraft.util.org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;
import org.spigotmc.AsyncCatcher;
import org.spigotmc.WatchdogThread;

import java.io.File;
import java.io.IOException;
import java.util.function.Function;

import static java.lang.String.format;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/26/2017 (1:23 PM)
 */
@Singleton
@StartParallel
public class Updater
{

    /** the file that restarts the server for us */
    private static final File RESTART_SCRIPT = new File("./start.sh");

    /** the file for the JAR that we'll be uploading */
    private static final Function<Player, File> UPDATE_FOR_DEV = player -> new File(format("/home/mc/update/dev/%s/Hyleria.jar", player.getName().toLowerCase()));

    /** name of this server */
    private final String serverName;

    /** our plugin */
    @Inject private Hyleria plugin;

    @Inject
    public Updater(RedisHandler redis, ServerConfig config)
    {
        this.serverName = config.name;
        // TODO(Ben): implement ServerUpdateRequestPayload
    }

    @Command ( executor = "update" )
    @Permission ( Role.ADMIN )
    public void baseCommand(Player player)
    {
        Message.prefix("Version").content("This server is currently running:").content(plugin.getDescription().getVersion(), ChatColor.YELLOW).send(player);
    }

    @SubCommand ( of = "update", executors = "dev" )
    @Permission ( value = Role.DEV, note = "Due to the nature of this command, only developers may use it" )
    public void devUpdate(Player player)
    {
        Message.prefix("Update").content(this.serverName, ChatColor.GREEN)
                                .content("will be right back after an update").send();

        try
        {
            FileUtils.forceDelete(new File("./plugins/Hyleria.jar"));
            FileUtils.copyFileToDirectory(UPDATE_FOR_DEV.apply(player), new File("./plugins"));
        }
        catch (IOException ex)
        {
            Issues.handle("Copy Plugin Update Jar", ex);
        }
        finally
        {
            Scheduler.delayed(this::restart, Time.SECONDS.toTicks(4));
        }
    }

    /**
     * Stop then start this server
     */
    private void restart()
    {
        try
        {
            AsyncCatcher.enabled = false;

            // stop the thread that watches for crashes
            WatchdogThread.doStop();
            Thread.sleep(100);

            // close connections
            MinecraftServer.getServer().getServerConnection().b();
            Thread.sleep(100);

            // alright, let's shut it down
            MinecraftServer.getServer().stop();

            final Thread _restartThread = new Thread(() ->
            {
                try
                {
                    Runtime.getRuntime().exec(new String[] { "/bin/sh", RESTART_SCRIPT.getPath() } );
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                    System.err.println("Unable to execute restart script");
                }
            });

            _restartThread.setDaemon(true);
            Runtime.getRuntime().addShutdownHook(_restartThread);
        }
        catch (Exception ex)
        {
            Issues.handle("Server Restart Attempt", ex);
        }
    }

}
