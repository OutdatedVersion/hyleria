package com.hyleria.coeus.available.uhcgames;

import com.google.inject.Inject;
import com.hyleria.Hyleria;
import com.hyleria.coeus.Coeus;
import com.hyleria.coeus.Game;
import com.hyleria.coeus.Status;
import com.hyleria.coeus.available.uhcgames.utils.ChestPopulator;
import com.hyleria.coeus.available.uhcgames.world.maps.UHCGamesMap;
import com.hyleria.common.math.Math;
import com.hyleria.common.reflect.ReflectionUtil;
import com.hyleria.util.*;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Jp78
 * @since Sunday Mar/2017
 */
public class UHCGames extends Game
{

    @Inject private Hyleria plugin;

    @Inject private ChestPopulator populator;
    private UHCGamesMap map;

    @Inject private Coeus engine;

    @SuppressWarnings ( "unchecked" )
    public void init(Coeus useless)
    {
        showKillerHeartsOnDeath = false;
        requiredPlayerCount = 20;

        final UHCGamesConfig config = loadConfig("uhc_games", UHCGamesConfig.class);

        List<UHCGamesMap> maps = config.maps.stream().map(name -> ReflectionUtil.classForName(getClass().getPackage().getName() + ".world.maps." + name)).map((Function<Class<?>, UHCGamesMap>) clazz -> plugin.boundInjection((Class<UHCGamesMap>) clazz)).collect(Collectors.toList());

        map = maps.get(Math.random(1, maps.size() + 1));
    }

    @Override
    public void begin()
    {
        engine.hookGameListener(); // Game's started. Time to enabled events
        World gameWorld = map.world(); // The game world, where everything takes place

        if (map.spawns().size() != map.allowedPlayers())
        {
            // TODO(Ben): you should be SILENTLY failing. players shouldn't ever have to deal with this!!
            MessageUtil.everyone(Colors.bold(ChatColor.RED) + "An Illegal State Exception has happened during the start. Please contact staff immediately");
            Issues.handle("UHCGMap inconsistency", new IllegalStateException("The map spawns and map allowed players aren't equal! This means there may not be enough spawns! Cancelling"));
            return;
        }

        engine.updateStatus(Status.PRE_GAME);

        // teleport
        Iterator<Location> spawnIterator = map.spawns().iterator();
        PlayerUtil.everyone().forEach(p -> p.teleport(spawnIterator.next()));

        MessageUtil.everyone(ChatColor.AQUA + "Game starting in 10 seconds!");

        Scheduler.delayed(() ->
        {
            engine.updateStatus(Status.ACTIVE_GAME);
            MessageUtil.everyone("Game started!");
        }, 10);
    }

    @Override
    public void end()
    {

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        e.getPlayer().setGameMode(GameMode.ADVENTURE); //Okay now they are in survival
        e.getPlayer().sendMessage(ChatColor.BLUE + "Welcome to UHC Games, you've been placed in spectator mode since the game has already started.");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockCreate(BlockPlaceEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE)
        {
            // Just to make sure they can't open chests :O
            event.setCancelled(true);
        }
    }

}
