package com.hyleria.coeus.damage;

import com.google.common.collect.Maps;
import com.hyleria.coeus.Game;
import com.hyleria.coeus.damage.kinds.*;
import com.hyleria.common.time.Tick;
import com.hyleria.util.Issues;
import com.hyleria.util.Module;
import com.hyleria.util.Scheduler;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.UUID;

import static com.hyleria.util.Colors.PLAYER;
import static com.hyleria.util.Colors.bold;
import static com.hyleria.util.VectorUtil.offset;
import static org.bukkit.Bukkit.getPluginManager;
import static org.bukkit.ChatColor.DARK_RED;
import static org.bukkit.ChatColor.GRAY;

/**
 * @author Ben (OutdatedVersion)
 * @since 10:37 PM (May/07/2016)
 */
public class DamageEventFactory extends Module
{

    // TODO(Ben): look over, and clean up this old af code

    /** a formatter that turns precision numbers to the tenths spot */
    private static final DecimalFormat TENTHS = new DecimalFormat("#.#");

    /** our game */
    private Game game;

    /** the log */
    private Map<UUID, DamageLog> logRelation;

    public DamageEventFactory(Game game)
    {
        this.game = game;
        this.logRelation = Maps.newHashMap();
    }

    /**
     * @param player the player
     *
     * @return the log bound to the provided player
     */
    public DamageLog logFor(Player player)
    {
        return logRelation.get(player.getUniqueId());
    }

    @EventHandler
    public void createLogs(PlayerJoinEvent event)
    {
        logRelation.put(event.getPlayer().getUniqueId(), new DamageLog(event.getPlayer()));
    }

    @EventHandler
    public void destoryLogs(PlayerQuitEvent event)
    {
        logRelation.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void processDeath(PlayerDeathEvent event)
    {
        try
        {
            Player _player = event.getEntity();
            DamageLog _log = logRelation.get(_player.getUniqueId());

            event.setDeathMessage(null);
            event.getDrops().clear();

            _player.setHealth(20.0D);

            Player _killer = _player.getKiller();

            if (_killer == null)
            {
                DamageDeathEvent _event = new DamageDeathEvent(_player, _log);
                getPluginManager().callEvent(_event);

                if (game.announceDeath)
                    announceDeath(_player);
            }
            else
            {
                CombatDeathEvent _event = new CombatDeathEvent(_player, _killer, _log);
                getPluginManager().callEvent(_event);

                if (game.announceDeath)
                    announceHomicide(_player, _killer);
            }

            // send(_player, C.RedBold + "YOU DIED", (_killer != null ? (C.Gray + "killed by " + _killer.getName()) : ""), 77);

            if (game.deathHandler == null)
            {
                if (_killer != null)
                {
                    if (game.showKillerHeartsOnDeath)
                    {
                        _player.sendMessage(bold(PLAYER) + _killer.getName() + GRAY + " had " + DARK_RED +
                                TENTHS.format(_killer.getHealth() / 2) + " ❤" + GRAY + " hearts left.");
                    }
                }

                // players are removed when they die
                // TODO(Ben): move this
                Scheduler.delayed(() -> _player.kickPlayer("dc"), Tick.SECONDS.toTicks(20));
            }
            else game.deathHandler.accept(event);
        }
        catch (Exception e)
        {
            Issues.handle("Process Player Death", e);
        }
    }


    @EventHandler
    public void processRanged(EntityDamageByEntityEvent event)
    {
        if (!event.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE))
            return;

        if (!(event.getEntity() instanceof Player))
            return;

        Player _victim = ((Player) event.getEntity());
        DamageLog _log = logRelation.get(_victim.getUniqueId());

        Projectile _projectile = ((Projectile) event.getDamager());

        if (!(_projectile.getShooter() instanceof Player))
            return;

        Player _attacker = ((Player) _projectile.getShooter());

        ItemStack _possibleBow = _attacker.getInventory().getItemInHand();

        Map<Enchantment, Integer> _enchants = null;

        if (_possibleBow != null && _possibleBow.getType().equals(Material.BOW))
            _enchants = _possibleBow.getEnchantments();

        RangedCombatEvent _event = new RangedCombatEvent(_victim, _attacker, event.getFinalDamage(), ((int) offset(_victim.getLocation(), _attacker.getLocation())), _enchants).call(RangedCombatEvent.class);

        if (_event.isCancelled())
            event.setCancelled(true);
        else
            _log.addEvent(_event);
    }

    @EventHandler
    public void processEvP(EntityDamageByEntityEvent event)
    {
        if (!(event.getEntity() instanceof Player))
            return;

        if (!event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK))
            return;

        Player _victim = ((Player) event.getEntity());
        DamageLog _log = logRelation.get(_victim.getUniqueId());
        Entity _damager = event.getDamager();

        MobAttackPlayerEvent _event = new MobAttackPlayerEvent(_victim, _damager, event.getFinalDamage()).call(MobAttackPlayerEvent.class);

        if (_event.isCancelled())
            event.setCancelled(true);
        else
            _log.addEvent(_event);
    }

    @EventHandler
    public void processPvE(EntityDamageByEntityEvent event)
    {
        if (!(event.getDamager() instanceof Player))
            return;

        Entity _damager = event.getDamager();

        PlayerAttackMobEvent _event = new PlayerAttackMobEvent(((Player) event.getDamager()), _damager, event.getFinalDamage()).call(PlayerAttackMobEvent.class);

        if (_event.isCancelled())
            event.setCancelled(true);
    }


    @EventHandler
    public void processCombat(EntityDamageByEntityEvent event)
    {
        if (!(event.getEntity() instanceof Player))
            return;

        if (!(event.getDamager() instanceof Player))
            return;

        Player _victim = (Player) event.getEntity();
        Player _attacker = (Player) event.getDamager();
        double _damage = event.getFinalDamage();
        DamageLog _log = logRelation.get(_victim.getUniqueId());

        final ItemStack _weapon = _attacker.getInventory().getItemInHand();

        if (_weapon != null)
        {
            ArmedCombatEvent _event = new ArmedCombatEvent(_victim, _attacker, _damage, _weapon).call(ArmedCombatEvent.class);

            if (_event.isCancelled())
                event.setCancelled(true);
            else
                _log.addEvent(_event);
        }
        else
        {
            DisarmedCombatEvent _event = new DisarmedCombatEvent(_victim, _attacker, _damage).call(DisarmedCombatEvent.class);

            if (_event.isCancelled())
                event.setCancelled(true);
            else
                _log.addEvent(_event);
        }
    }

    @EventHandler
    public void generalDamage(EntityDamageEvent event)
    {
        if (!(event.getEntity() instanceof Player))
            return;

        // Contact = Cactus
        Player _player = ((Player) event.getEntity());
        double _damage = event.getFinalDamage();
        DamageLog _log = logRelation.get(_player.getUniqueId());

        switch (event.getCause())
        {
            case FIRE:
            {
                FireDamageEvent _event = new FireDamageEvent(_player, _damage).call(FireDamageEvent.class);

                if (_event.isCancelled())
                    event.setCancelled(true);
                else
                    _log.addEvent(_event);

                return;
            }

            case LAVA:
            {
                LavaDamageEvent _event = new LavaDamageEvent(_player, _damage).call(LavaDamageEvent.class);

                if (_event.isCancelled())
                    event.setCancelled(true);
                else
                    _log.addEvent(_event);

                return;
            }

            case STARVATION:
            {
                StarvationDamageEvent _event = new StarvationDamageEvent(_player, _damage).call(StarvationDamageEvent.class);

                if (_event.isCancelled())
                    event.setCancelled(true);
                else
                    _log.addEvent(_event);

                return;
            }

            case FALL:
            {
                PlayerTakeFallDamageEvent _event = new PlayerTakeFallDamageEvent(_player, _damage).call(PlayerTakeFallDamageEvent.class);

                if (_event.isCancelled())
                    event.setCancelled(true);
                else
                    _log.addEvent(_event);

                return;
            }

            case VOID:
            {
                PlayerVoidDamageEvent _event = new PlayerVoidDamageEvent(_player, _damage).call(PlayerVoidDamageEvent.class);

                if (_event.isCancelled())
                    event.setCancelled(true);
                else
                    _log.addEvent(_event);

                return;
            }

            case SUFFOCATION:
            {
                PlayerSuffocateEvent _event = new PlayerSuffocateEvent(_player, _damage).call(PlayerSuffocateEvent.class);

                if (_event.isCancelled())
                    event.setCancelled(true);
                else
                    _log.addEvent(_event);

                return;
            }

            case DROWNING:
            {
                PlayerDrownEvent _event = new PlayerDrownEvent(_player, _damage).call(PlayerDrownEvent.class);

                if (_event.isCancelled())
                    event.setCancelled(true);
                else
                    _log.addEvent(_event);
            }

        }
    }

    /* ==============
          Messages
       ============== */

    // TODO(Ben): add the messages here

    private void announceDeath(Player player)
    {

    }

    private void announceHomicide(Player player, Player killer)
    {

    }

}
