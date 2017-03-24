package com.hyleria.coeus.available.uhc;

import com.google.inject.Inject;
import com.hyleria.Hyleria;
import com.hyleria.coeus.Coeus;
import com.hyleria.coeus.Game;
import com.hyleria.coeus.available.uhc.scenario.UHCScenario;
import com.hyleria.coeus.available.uhc.world.Border;
import com.hyleria.coeus.damage.CombatEvent;
import com.hyleria.coeus.scoreboard.PlayerScoreboard;
import com.hyleria.command.api.CommandHandler;
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

import java.util.Optional;
import java.util.Set;
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

    /** */
    private Location spawnLocation;

    private Border border;

    /** whether or not we allow combat right now */
    // TODO(Ben): replace with some game flag
    private boolean pvpEnabled = false;

    private long startedAt = -1;

    private int shrinkStage = 0;

    @Override
    public void init(Coeus engine)
    {
        // just for now
        scoreboardTitle = "Hyleria EU";

        config = loadConfig("uhc", UHCConfig.class);
        ReflectionUtil.printOut(config);

        world = WorldCreator.name("uhc").createWorld();
        lobby = WorldCreator.name("uhc_lobby").createWorld();
        spawnLocation = new Location(Bukkit.getWorld("uhc_lobby"), 1.5, 5, -.5, 55.3f, 5.3f);

        plugin.registerListeners(border = new Border().init(config.apothem).generatePhysicalBorder());

        // add commands
        plugin.get(CommandHandler.class).registerCommandsFromObject(UHCCommands.class);

        // handle start by host commands
        requiredPlayerCount = -1;

        preGameJoinHandler = event ->
        {
            event.setJoinMessage(null);
            event.getPlayer().teleport(spawnLocation);
            // TODO(Ben): eventually spread players out among the spawn
        };
    }

    @Override
    public void begin()
    {
        System.out.println("[UHC] Starting player spread");

        // one player must be in the game world first at 0, 0 for spreading w/ Minecraft's command
        final Player _dummy = PlayerUtil.everyone().get(0);
        _dummy.teleport(border.origin);

        System.out.println("[UHC] Teleport dummy: " + _dummy.getName());

        final String _command = "spreadplayers 0 0 "
                + (config.apothem / 4) + " " + (config.apothem / 2) + " false " +
                PlayerUtil.everyone().stream().collect(StringBuilder::new, (builder, player) -> builder.append(player.getName()).append(" "), StringBuilder::append);

        System.out.println("[UHC] Spread command: [" + _command + "]");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), _command.trim());


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

        System.out.println("Till shrink in ticks: " + Time.MINUTES.toTicks(config.timeTillBorderShrink));
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
            scoreboard.write(startedAt == -1 ? "Not Started" : TimeUtil.niceTimeFormat(System.currentTimeMillis() - startedAt));
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
        System.out.println("ATTEMPT SHRINK");

        if (config.shrinkFactorProgression.length >= shrinkStage + 1)
        {
            border.shrink(config.shrinkFactorProgression[shrinkStage++]);

            PlayerUtil.play(Sound.WITHER_SPAWN);
            MessageUtil.everyone(bold(GOLD) + "The border has shrunk!");
            MessageUtil.everyone(bold(GRAY) + "Now at: " + bold(YELLOW) + border.region().getLength() + "x" + border.region().getWidth());
        }
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
