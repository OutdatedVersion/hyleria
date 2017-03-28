package com.hyleria.coeus.available.uhcgames;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.hyleria.Hyleria;
import com.hyleria.coeus.Coeus;
import com.hyleria.coeus.Game;
import com.hyleria.coeus.Status;
import com.hyleria.coeus.available.uhcgames.utils.ChestPopulator;
import com.hyleria.coeus.available.uhcgames.utils.Tier;
import com.hyleria.coeus.available.uhcgames.world.maps.UHCGamesMap;
import com.hyleria.coeus.scoreboard.PlayerScoreboard;
import com.hyleria.common.math.Math;
import com.hyleria.common.reflect.ReflectionUtil;
import com.hyleria.common.util.Wrapper;
import com.hyleria.util.Issues;
import com.hyleria.util.MessageUtil;
import com.hyleria.util.PlayerUtil;
import com.hyleria.util.Scheduler;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
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

    private List<UHCGamesMap> failover;
    @Inject private Coeus engine;

    private Long time = 1500L;
    private Long refillTime = 300L;

    @SuppressWarnings ( "unchecked" )
    public void init(Coeus useless)
    {
        engine.updateStatus(Status.INIT); //Loading maps and doing resource things
        showKillerHeartsOnDeath = false;
        requiredPlayerCount = 20;

        final UHCGamesConfig config = loadConfig("uhc_games", UHCGamesConfig.class);

        List<UHCGamesMap> maps = config.maps.stream().map(name -> ReflectionUtil.classForName(getClass().getPackage().getName() + ".world.maps." + name)).map((Function<Class<?>, UHCGamesMap>) clazz -> plugin.boundInjection((Class<UHCGamesMap>) clazz)).collect(Collectors.toList());

        engine.updateStatus(Status.MAP_FETCH);
        map = maps.get(Math.random(1, maps.size() + 1));
        failover = Lists.newArrayList(maps);
        failover.remove(map); //Make sure the engine doesn't use the broken map twice
        World gameWorld = map.world(); // The game world, where everything takes place
        while (map.spawns().size() != map.allowedPlayers())
        {
            if (failover.size() == 0)
            {
                //We have checked all the available maps and none work. This game is now failing. Cancel the game!
                engine.updateStatus(Status.END_GAME); //The game is done. Destroy the server
                System.exit(1); //Somethings gone wrpng soo
            }
            map = failover.get(Math.random(1, failover.size() + 1));
            failover.remove(map); //
            Issues.handle("UHCGMap inconsistency", new IllegalStateException("The map spawns and map allowed players aren't equal! This means there may not be enough spawns! Changing map"));
        }
    }

    @Override
    public void begin()
    {
        engine.hookGameListener(); // Game's started. Time to enabled events
        World gameWorld = map.world(); // The game world, where everything takes place

        engine.updateStatus(Status.PRE_GAME); //Games on!

        // teleport
        Iterator<Location> spawnIterator = map.spawns().iterator();
        PlayerUtil.everyone().forEach(p -> p.teleport(spawnIterator.next()));

        MessageUtil.everyone(ChatColor.AQUA + "Game starting in 10 seconds!");

        final Wrapper<Integer> timer = new Wrapper<>();
        timer.set(30);//30 seconds to start
        engine.updateStatus(Status.PRE_GAME); //Lets start the countdown!
        Scheduler.loopUntil(() ->
        {
            int temp = timer.get() - 1;
            timer.set(temp);
            MessageUtil.everyone(ChatColor.RED + "Game starting in " + temp);
        }, () ->
        {
            MessageUtil.everyone(ChatColor.BLUE + "Game started!");
            engine.updateStatus(Status.ACTIVE_GAME);
        }, 1, 10);
        Scheduler.loopUntil(() ->
        {
            populator.populated.clear();
            MessageUtil.everyone(ChatColor.GREEN + "Chests have been refilled!");
        },null,300,5); //Refill the chests / make em refillable

    }

    @Override
    public void end()
    {

    }

    @Override
    public void updateScoreboard(PlayerScoreboard scoreboard)
    {
        if (engine.status() == Status.ACTIVE_GAME)
        {
            time--; //Time is minus every second
            if(refillTime == 0) refillTime = 300L; else refillTime--;
        } scoreboard.purge();
        scoreboard.title(ChatColor.GREEN + "UHC Games");
        scoreboard.blank();
        scoreboard.writeHead(ChatColor.DARK_RED + "Time till death match");
        scoreboard.write(ChatColor.RED + String.valueOf(TimeUnit.SECONDS.toMinutes(time)) + " Minutes");
        scoreboard.blank();
        scoreboard.writeHead(ChatColor.BLUE + "Time till chest refill");
        scoreboard.write(ChatColor.WHITE + String.valueOf(TimeUnit.SECONDS.toMinutes(refillTime) + " Minutes"));
        scoreboard.blank();
        scoreboard.writeURL();
        scoreboard.draw();

        super.updateScoreboard(scoreboard);
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
        if (event.getPlayer().getGameMode().equals(GameMode.ADVENTURE))
        {
            // Just to make sure they can't open chests :O
            event.setCancelled(true);
        }
        if (event.getClickedBlock().getType().equals(Material.CHEST))
        {
            //Chest opened
            event.setCancelled(true); //Cancel it
            if (map.highChests().contains(event.getClickedBlock().getLocation()))
            {
                //its a tier 2
                populator.populate((Chest) event.getClickedBlock(), Tier.HIGH); //Populate the chest
                event.setCancelled(false);
                return;
            }
            Tier tier = Math.chance(50) ? Tier.LOW : Tier.BAD; // chest tier
            populator.populate((Chest) event.getClickedBlock(), tier);
            event.setCancelled(false);
            return;
        }
        if (event.getClickedBlock().getType().equals(Material.TRAPPED_CHEST))
        {
            event.setCancelled(true);
            populator.populate((Chest) event.getClickedBlock(), Tier.HIGH);
            event.setCancelled(false);
        }

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e)
    {
        if (e.getPlayer().getGameMode().equals(GameMode.ADVENTURE))
            return; //Spectators
        if (engine.status().equals(Status.PRE_GAME))
        {
            e.setCancelled(true); //Cant move now!
        }
    }

}
