package com.hyleria.coeus.available.uhcg;

import com.google.inject.Inject;
import com.hyleria.Hyleria;
import com.hyleria.coeus.Coeus;
import com.hyleria.coeus.Game;
import com.hyleria.coeus.Status;
import com.hyleria.coeus.available.uhcg.utils.ChestPopulator;
import com.hyleria.coeus.available.uhcg.world.maps.UHCGMap;
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
 * @since Sunday March/2017
 */
public class UHCGames extends Game
{

    @Inject
    private Hyleria plugin;

    @Inject
    private ChestPopulator populator;
    private UHCGMap map;

    @Inject
    private Coeus engine;

    @SuppressWarnings ( "unchecked" )
    public void init(Coeus useless)
    {

        //We've been initialized! Lets set our player spawn points :)
        showKillerHeartsOnDeath = false; //That would be unfair
        requiredPlayerCount = 20;
        UHCGConfig config = loadConfig("UHCG", UHCGConfig.class);
        List<UHCGMap> maps = config.maps.stream().map(name -> ReflectionUtil.classForName(getClass().getPackage().getName() + ".world.maps." + name)).map((Function<Class<?>, UHCGMap>) clazz -> plugin.boundInjection((Class<UHCGMap>) clazz)).collect(Collectors.toList());
        map = maps.get(Math.random(1, maps.size() + 1));

    }

    @Override
    public void begin()
    {
        engine.hookGameListener(); //Game's started. Time to enabled events
        World gameWorld = map.world(); //The game world, where everything takes place
        if (map.spawns().size() != map.allowedPlayers())
        {
            //WOAH. This shouldn't be possible! Let's tell em that
            MessageUtil.everyone(Colors.bold(ChatColor.RED) + "An Illegal State Exception has happened during the start. Please contact staff immediately");
            Issues.handle("UHCGMap inconsistency", new IllegalStateException("The map spawns and map allowed players aren't equal! This means there may not be enough spawns! Cancelling"));
            return;
        }
        Iterator<Location> spawnIterator = map.spawns().iterator(); //Get us a spawn iterator
        engine.updateStatus(Status.PRE_GAME); //Game has yet to start
        PlayerUtil.everyone().forEach(p -> p.teleport(spawnIterator.next())); //Teleport
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
        e.getPlayer().sendMessage(ChatColor.BLUE + "Welcome to the UHC Games, you've been placed in spectator mode since the game has already started.");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e)
    {
        e.setCancelled(true); //Can't have us breakin' blocks
    }

    @EventHandler
    public void onBlockCreate(BlockPlaceEvent e)
    {
        e.setCancelled(true); //Can't place em either
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e)
    {
        e.get
        if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE))
        {
            e.setCancelled(true); //Just to make sure they can't open chests :O
        }
    }

}
