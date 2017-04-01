package com.hyleria.coeus.available.uhc;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.hyleria.Hyleria;
import com.hyleria.coeus.Coeus;
import com.hyleria.coeus.Game;
import com.hyleria.coeus.available.uhc.scenario.UHCScenario;
import com.hyleria.coeus.available.uhc.world.Border;
import com.hyleria.coeus.damage.CombatEvent;
import com.hyleria.coeus.scoreboard.PlayerScoreboard;
import com.hyleria.command.api.CommandHandler;
import com.hyleria.common.math.Math;
import com.hyleria.common.reflect.ReflectionUtil;
import com.hyleria.common.time.Time;
import com.hyleria.common.time.TimeUtil;
import com.hyleria.util.MessageUtil;
import com.hyleria.util.PlayerUtil;
import com.hyleria.util.Scheduler;
import com.hyleria.util.TextUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.hyleria.util.Colors.bold;
import static org.bukkit.ChatColor.*;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/08/2017 (5:42 PM)
 */
public class UHC extends Game
{

    /** our plugin */
    @Inject private Hyleria plugin;

    /** details for this UHC instance */
    private UHCConfig config;

    /** the world the UHC is taking place on */
    private World world;

    /** where players will go before the game starts */
    private World lobby;

    /** where player's go when joining this server */
    private Location spawnLocation;

    /** our border */
    private Border border;

    /** whether or not we allow combat right now */
    // TODO(Ben): replace with some game flag
    private boolean pvpEnabled = false;

    /** when the game started */
    private long startedAt = -1;

    /** how many times we've shrinked the border */
    private int shrinkStage = 0;

    /** where players will go when the scatter starts */
    private List<Vector> playerSpawnLocations = Lists.newArrayList();

    @Override
    public void init(Coeus engine)
    {
        // just for now
        scoreboardTitle = "Hyleria UHC";

        config = loadConfig("uhc", UHCConfig.class);
        ReflectionUtil.printOut(config);

        world = WorldCreator.name("uhc").createWorld();
        lobby = WorldCreator.name("uhc_lobby").createWorld();
        spawnLocation = new Location(lobby, 1.5, 5, -.5, 55.3f, 5.3f);

        plugin.registerListeners(border = new Border().init(config.borderDistance).generatePhysicalBorder());

        // handle start by host commands
        requiredPlayerCount = -1;

        preGameJoinHandler = event ->
        {
            event.setJoinMessage(null);
            event.getPlayer().teleport(spawnLocation);
            // TODO(Ben): eventually spread players out among the spawn

            reserveLocationFor(event.getPlayer());
        };
    }

    @Override
    public void begin()
    {
        // add commands
        plugin.get(CommandHandler.class).register(UHCCommands.class);

        System.out.println("[UHC] Starting player spread");


        // I only had a short amount of time to complete this spread
        // I'd like for it to be much different in favor of performance
        // It wasn't an option then, so you get this disappointing
        // solution instead.

        final List<? extends Player> _players = PlayerUtil.everyone();
        final AtomicInteger _count = new AtomicInteger(_players.size());

        int _id = Scheduler.timerExact(() ->
        {
            final Location _location = playerSpawnLocations.remove(0).toLocation(world);
            _location.setY(world.getHighestBlockYAt(_location));
            _location.getChunk().load();

            Scheduler.delayed(() -> _players.get(_count.getAndDecrement() - 1).teleport(_location), 20);
        }, 0, 2);

         Scheduler.endAfter(_id, 12 * _players.size());

        // teleport into cage
        // break cages when everyone has been relocated

        startedAt = System.currentTimeMillis();
        MessageUtil.everyone(bold(GREEN) + "We've started...");

        // load scenarios
        final Set<UHCScenario> _scenarios = config.enabledScenarios
                .stream()
                .map(name -> ReflectionUtil.classForName(getClass().getPackage().getName() + ".scenario." + name))
                .map((Function<Class<?>, UHCScenario>) clazz -> plugin.boundInjection((Class<UHCScenario>) clazz))
                .collect(Collectors.toSet());

        _scenarios.forEach(UHCScenario::init);


        // reset player's health after the provided time
        Scheduler.delayed(() ->
        {
            PlayerUtil.everyone().forEach(PlayerUtil::fullHealth);
            MessageUtil.everyone(bold(GREEN) + "You've all been healed.");
        }, Time.MINUTES.toTicks(config.healTime));

        // allow PvP
        Scheduler.delayed(this::togglePvP, Time.MINUTES.toTicks(config.pvpTime));

        System.out.println("[UHC] Till shrink in ticks: " + Time.MINUTES.toTicks(config.timeTillBorderShrink));
        Scheduler.timer(this::shrinkBorder, config.timeTillBorderShrink * 60);
    }

    @Override
    public void end()
    {

    }

    @Override
    public void updateScoreboard(PlayerScoreboard scoreboard)
    {
        scoreboard.blank();

        if (startedAt == -1)
        {
            scoreboard.write("i need");
            scoreboard.write("something");
            scoreboard.write("to");
            scoreboard.write("write");
            scoreboard.write("here");
            scoreboard.write("!!");
        }
        else
        {
            scoreboard.writeHead("Game Time");
            scoreboard.write(TimeUtil.niceTimeFormat(System.currentTimeMillis() - startedAt));
            scoreboard.blank();
            scoreboard.write("Alive", PlayerUtil.onlineCount());
            scoreboard.write("Watching", 0);
            scoreboard.blank();
            scoreboard.write("Kills", kills.containsKey(scoreboard.player().getUniqueId()) ? kills.get(scoreboard.player().getUniqueId()).size() : 0);
        }

        scoreboard.writeURL();
        scoreboard.draw();
    }

    /**
     * Switches PvP on/off & let's players know
     */
    private void togglePvP()
    {
        this.pvpEnabled = !this.pvpEnabled;
        MessageUtil.everyone(bold(WHITE) + "PvP is now " + TextUtil.enabledDisabledBold(this.pvpEnabled));
    }

    /**
     * Attempt to make the world border smaller
     */
    public void shrinkBorder()
    {
        if (config.shrinkFactorProgression.length >= shrinkStage + 1)
        {
            border.shrink(config.shrinkFactorProgression[shrinkStage++]);

            PlayerUtil.play(Sound.WITHER_SPAWN);
            MessageUtil.everyone(bold(GOLD) + "The border has shrunk!");
            MessageUtil.everyone(bold(GRAY) + "Now at: " + bold(YELLOW) + border.region().getLength() + "x" + border.region().getWidth());
        }
    }

    /**
     * Finds a spawn location for the
     * provided player
     *
     * @param player the player
     */
    private void reserveLocationFor(Player player)
    {
        // uses poisson-disc distribution via mitchell's best-candidate algo
        // THIS CODE IS COMPLETE SHIT (just warning you)

        Vector _bestChoice = new Vector();
        double _bestDistance = 0;

        for (int i = 0; i < 5; i++)
        {
            Vector _try = new Vector(Math.random(border.region().getWidth() / 2), 0, Math.random(border.region().getLength() / 2));

            if (playerSpawnLocations.contains(_try))
            {
                i--;
                continue;
            }

            Vector _closest = new Vector();
            double _closestDistance = 0;

            // should look into using the alternate solution
            // bad idea performance wise to be doing this
            for (Vector previous : playerSpawnLocations)
            {
                if (previous.distance(_closest) < _closestDistance)
                {
                    _closest = previous;
                    _closestDistance = previous.distance(_closest);
                }
            }

            if (_closest.distance(_try) > _bestDistance)
            {
                _bestDistance = _closest.distance(_try);
                _bestChoice = _try;
            }
        }

        playerSpawnLocations.add(_bestChoice);
    }

    @EventHandler
    public void handlePvP(CombatEvent event)
    {
        if (!pvpEnabled)
            event.setCancelled(true);
    }

    @EventHandler
    public void disallowConsumeOfStrength(PlayerItemConsumeEvent event)
    {
        // probably a better way to do all of this?
        // may need to listen for the linger one too at some point

        if (event.getItem().getType() == Material.POTION)
        {
            final PotionMeta _meta = (PotionMeta) event.getItem().getItemMeta();
            final Optional<PotionEffect> _effect = _meta.getCustomEffects().stream().filter(effect -> effect.getType() == PotionEffectType.INCREASE_DAMAGE).findFirst();

            if (_effect.isPresent())
            {
                if (!config.allowStrengthOne && _effect.get().getDuration() == 1)
                {
                    event.setCancelled(true);
                    event.setItem(null);
                }

                if (!config.allowStrengthTwo && _effect.get().getDuration() == 2)
                {
                    event.setCancelled(true);
                    event.setItem(null);
                }
            }
        }
    }

    @EventHandler
    public void disableNether(EntityCreatePortalEvent event)
    {
        if (!config.nether)
            event.setCancelled(true);
    }

}
