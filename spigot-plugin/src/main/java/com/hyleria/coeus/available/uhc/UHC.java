package com.hyleria.coeus.available.uhc;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hyleria.Hyleria;
import com.hyleria.coeus.Coeus;
import com.hyleria.coeus.Game;
import com.hyleria.coeus.available.uhc.scenario.UHCScenario;
import com.hyleria.coeus.available.uhc.world.Border;
import com.hyleria.coeus.damage.CombatEvent;
import com.hyleria.coeus.scoreboard.PlayerScoreboard;
import com.hyleria.common.reflect.ReflectionUtil;
import com.hyleria.common.time.Time;
import com.hyleria.util.MessageUtil;
import com.hyleria.util.PlayerUtil;
import com.hyleria.util.Scheduler;
import com.hyleria.util.TextUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.hyleria.util.Colors.bold;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.WHITE;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/08/2017 (5:42 PM)
 */
@Singleton
public class UHC extends Game
{

    /** */
    @Inject private Hyleria plugin;

    /**  */
    private UHCConfig config;

    /**  */
    private World world;

    private boolean pvpEnabled = false;

    // handle stats engine side

    @Override
    public void init(Coeus engine)
    {
        config = loadConfig("uhc", UHCConfig.class);
        ReflectionUtil.printOut(config);

        world = WorldCreator.name("uhc").createWorld();

        plugin.registerListeners(new Border().init(config.apothem).generatePhysicalBorder());
    }

    @Override
    public void begin()
    {
        MessageUtil.everyone(bold(GREEN) + "nifty starting message!!");

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
            MessageUtil.everyone(bold(ChatColor.GOLD) + "You've all been healed.");
        }, Time.MINUTES.toTicks(config.healTime));

        // allow PvP
        Scheduler.delayed(this::togglePvP, Time.MINUTES.toTicks(config.pvpTime));
    }

    @Override
    public void end()
    {

    }

    @Override
    public void updateScoreboard(PlayerScoreboard scoreboard)
    {
        scoreboard.blank();
        scoreboard.writeHead("Today");
        scoreboard.write(ChatColor.RED + "Mar/15/17");
        scoreboard.blank();
        scoreboard.writeHead("Players");
        scoreboard.write(ChatColor.RED + "1");
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

}
