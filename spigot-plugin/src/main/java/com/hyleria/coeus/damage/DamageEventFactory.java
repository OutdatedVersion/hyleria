package com.simplexitymc.kraken.damage;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.simplexitymc.kraken.Game;
import com.simplexitymc.kraken.Kraken;
import com.simplexitymc.kraken.damage.kinds.*;
import com.simplexitymc.module.Module;
import com.simplexitymc.util.*;
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

import java.util.*;

/**
 * OutdatedVersion
 * At: 10:37 PM (May/07/2016)
 * cave-realms
 */

public class DamageEventFactory extends Module
{

    private HashMap<UUID, DamageLog> logRelation;

    public DamageEventFactory()
    {
        super("Damage Factory");

        logRelation = Maps.newHashMap();
    }

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
            Game _game = Kraken.get().current();

            event.setDeathMessage(null);

            if (!_game.dropsOnDeath)
                event.getDrops().clear();

            _player.setHealth(20.0D);

            Player _killer = _player.getKiller();

            if (_killer == null)
            {
                DamageDeathEvent _event = new DamageDeathEvent(_player, _log);
                ServerUtil.fireEvent(_event);

                if (_game.announceDeath)
                    announceDeath(_player);
            }
            else
            {
                CombatDeathEvent _event = new CombatDeathEvent(_player, _killer, _log);
                ServerUtil.fireEvent(_event);

                if (_game.announceDeath)
                    announceHomicide(_player, _killer);
            }

            if (_game.singleLife)
                _game.removePlayer(_player);

            if (! _game.skipDeathTitle)
                TitleUtil.send(_player, C.RedBold + "YOU DIED", (_killer != null ? (C.Gray + "killed by " + _killer.getName()) : ""), 77);

            if (_game.deathHandler == null)
            {
                if (_game.isActive() && _game.singleLife)
                {
                    if (_killer != null)
                    {
                        if (_game.showKillerHealthOnDeath)
                        {
                            String _hearts = WordUtil.toTenths(_killer.getHealth() / 2);
                            MessageUtil.m(_player, "Hearts", C.Player + _killer.getName() + C.Gray + " had " + C.DRed + _hearts + " ❤" + C.Gray + " hearts left.");
                        }

                        Scheduler.delayed(() -> PlayerUtil.resetAsSpec(_player), 3);
                    }
                    else
                        Scheduler.delayed(() -> PlayerUtil.resetAsSpec(_player), 3);

                    if (_player.getLocation().getY() < 7)
                        _player.teleport(_game.map().getSpectatorSpawn());
                    else
                        _player.teleport(_player.getLocation().add(0, 1.5, 0));
                }
                else if (!_game.isActive())
                    _player.teleport(Kraken.get().lobby().spawn());
            }
            else
                _game.deathHandler.accept(event);
        }
        catch (Exception e)
        {
            Issues.handle("Process Player Death", e);
        }
    }


    @EventHandler
    public void processRanged(EntityDamageByEntityEvent event)
    {
        if (! event.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE))
            return;

        if (! (event.getEntity() instanceof Player))
            return;

        Player _victim = ((Player) event.getEntity());
        DamageLog _log = logRelation.get(_victim.getUniqueId());

        Projectile _projectile = ((Projectile) event.getDamager());

        if (! (_projectile.getShooter() instanceof Player))
            return;

        Player _attacker = ((Player) _projectile.getShooter());

        ItemStack _possibleBow = _attacker.getInventory().getItemInMainHand();

        Map<Enchantment, Integer> _enchants = null;

        if (_possibleBow != null && _possibleBow.getType().equals(Material.BOW))
            _enchants = _possibleBow.getEnchantments();

        RangedCombatEvent _event = new RangedCombatEvent(_victim, _attacker, event.getFinalDamage(), ((int) MathUtil.offset(_victim.getLocation(), _attacker.getLocation())), _enchants);

        ServerUtil.fireEvent(_event);

        if (_event.isCancelled())
            event.setCancelled(true);
        else
            _log.addEvent(_event);
    }

    @EventHandler
    public void processEvP(EntityDamageByEntityEvent event)
    {
        if (! (event.getEntity() instanceof Player))
            return;

        if (! event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK))
            return;

        Player _victim = ((Player) event.getEntity());
        DamageLog _log = logRelation.get(_victim.getUniqueId());
        Entity _damager = event.getDamager();

        MobAttackPlayerEvent _event = new MobAttackPlayerEvent(_victim, _damager, event.getFinalDamage());
        ServerUtil.fireEvent(_event);

        if (_event.isCancelled())
            event.setCancelled(true);
        else
            _log.addEvent(_event);
    }

    @EventHandler
    public void processPvE(EntityDamageByEntityEvent event)
    {
        if (! (event.getDamager() instanceof Player))
            return;

        Entity _damager = event.getDamager();

        PlayerAttackMobEvent _event = new PlayerAttackMobEvent(((Player) event.getDamager()), _damager, event.getFinalDamage());
        ServerUtil.fireEvent(_event);

        if (_event.isCancelled())
            event.setCancelled(true);
    }


    @EventHandler
    public void processCombat(EntityDamageByEntityEvent event)
    {
        if (! (event.getEntity() instanceof Player))
            return;

        if (! (event.getDamager() instanceof Player))
            return;

        Player _victim = ((Player) event.getEntity());
        Player _attacker = ((Player) event.getDamager());
        double _damage = event.getFinalDamage();
        DamageLog _log = logRelation.get(_victim.getUniqueId());

        ItemStack _weapon = _attacker.getInventory().getItemInMainHand();
        if (_weapon == null)
            _weapon = _attacker.getInventory().getItemInOffHand();

        if (_weapon != null)
        {
            ArmedCombatEvent _event = new ArmedCombatEvent(_victim, _attacker, _damage, _weapon);
            ServerUtil.fireEvent(_event);

            if (_event.isCancelled())
                event.setCancelled(true);
            else
                _log.addEvent(_event);
        }
        else
        {
            DisarmedCombatEvent _event = new DisarmedCombatEvent(_victim, _attacker, _damage);
            ServerUtil.fireEvent(_event);

            if (_event.isCancelled())
                event.setCancelled(true);
            else
                _log.addEvent(_event);
        }
    }

    @EventHandler
    public void generalDamage(EntityDamageEvent event)
    {
        if (! (event.getEntity() instanceof Player))
            return;

        // Contact = Cactus
        Player _player = ((Player) event.getEntity());
        double _damage = event.getFinalDamage();
        DamageLog _log = logRelation.get(_player.getUniqueId());

        switch ( event.getCause() )
        {
            case FIRE:
            {
                FireDamageEvent _event = new FireDamageEvent(_player, _damage);
                ServerUtil.fireEvent(_event);

                if (_event.isCancelled())
                    event.setCancelled(true);
                else
                    _log.addEvent(_event);

                return;
            }

            case LAVA:
            {
                LavaDamageEvent _event = new LavaDamageEvent(_player, _damage);
                ServerUtil.fireEvent(_event);

                if (_event.isCancelled())
                    event.setCancelled(true);
                else
                    _log.addEvent(_event);

                return;
            }

            case STARVATION:
            {
                StarvationDamageEvent _event = new StarvationDamageEvent(_player, _damage);
                ServerUtil.fireEvent(_event);

                if (_event.isCancelled())
                    event.setCancelled(true);
                else
                    _log.addEvent(_event);

                return;
            }

            case FALL:
            {
                PlayerTakeFallDamageEvent _event = new PlayerTakeFallDamageEvent(_player, _damage);
                ServerUtil.fireEvent(_event);

                if (_event.isCancelled())
                    event.setCancelled(true);
                else
                    _log.addEvent(_event);

                return;
            }

            case VOID:
            {
                PlayerVoidDamageEvent _event = new PlayerVoidDamageEvent(_player, _damage);
                ServerUtil.fireEvent(_event);

                if (_event.isCancelled())
                    event.setCancelled(true);
                else
                    _log.addEvent(_event);

                return;
            }

            case SUFFOCATION:
            {
                PlayerSuffocateEvent _event = new PlayerSuffocateEvent(_player, _damage);
                ServerUtil.fireEvent(_event);

                if (_event.isCancelled())
                    event.setCancelled(true);
                else
                    _log.addEvent(_event);

                return;
            }

            case DROWNING:
            {
                PlayerDrownEvent _event = new PlayerDrownEvent(_player, _damage);
                ServerUtil.fireEvent(_event);

                if (_event.isCancelled())
                    event.setCancelled(true);
                else
                    _log.addEvent(_event);

                return;
            }
        }
    }

    /* ==============
          Messages
       ============== */

    private void announceDeath(Player player)
    {
        final EntityDamageEvent _damageCause = player.getLastDamageCause();
        final EntityDamageEvent.DamageCause _cause = _damageCause != null ? _damageCause.getCause() : null;

        List<String> _randomMessage = Lists.newArrayList();

        if (_cause != null)
        {
            switch ( _cause )
            {
                case BLOCK_EXPLOSION:
                    _randomMessage.add(C.Player + player.getName() + C.Gray + " died in an explosion.");
                    _randomMessage.add(C.Player + player.getName() + C.Gray + " ran near the big TNT pile.");
                    _randomMessage.add(C.Player + player.getName() + C.Gray + " died of explosioness!");
                    _randomMessage.add("RIP " + C.Player + player.getName() + C.Gray + ", he died in a super epic explosion.");
                    break;

                case CUSTOM:
                    _randomMessage.add(C.Player + player.getName() + C.Gray + " was eaten by the border.");
                    _randomMessage.add(C.Player + player.getName() + C.Gray + " died while checking out this border thingy.");
                    _randomMessage.add(C.Player + player.getName() + C.Gray + " was too curious about the border.");
                    _randomMessage.add(C.Player + player.getName() + C.Gray + " was killed by the border.");
                    break;

                case DROWNING:
                    _randomMessage.add(C.Player + player.getName() + C.Gray + " didn't read \"No Lifeguard on Duty\"!");
                    _randomMessage.add(C.Player + player.getName() + C.Gray + " never took swimming lessons.");
                    _randomMessage.add(C.Player + player.getName() + C.Gray + " drowned.");
                    break;

                case FALL:
                    _randomMessage.add(C.Player + player.getName() + C.Gray + " died of fall damage.");
                    _randomMessage.add(C.Player + player.getName() + C.Gray + " fell to their death.");
                    _randomMessage.add(C.Player + player.getName() + C.Gray + " thought they could make the jump.");
                    break;

                case FALLING_BLOCK:
                    _randomMessage.add(C.Player + player.getName() + C.Gray + " was killed by falling blocks.");
                    _randomMessage.add(C.Player + player.getName() + C.Gray + " ran under your average falling blocks.");
                    break;

                case FIRE:
                    _randomMessage.add(C.Player + player.getName() + C.Gray + " wanted to be one with the blazes.");
                    _randomMessage.add(C.Player + player.getName() + C.Gray + " died because of fire damage.");
                    _randomMessage.add(C.Player + player.getName() + C.Gray + " is not fire proof.");
                    break;

                case LAVA:
                    _randomMessage.add(C.Player + player.getName() + C.Gray + " died in lava.");
                    _randomMessage.add(C.Player + player.getName() + C.Gray + " took a hot bath.");
                    _randomMessage.add(C.Player + player.getName() + C.Gray + " thought the red water was just hot water.");
                    break;

                case MAGIC:
                    _randomMessage.add(C.Player + player.getName() + C.Gray + " died of magic.");
                    _randomMessage.add(C.Player + player.getName() + C.Gray + " messed up their magic trick.");
                    break;

                case SUFFOCATION:
                    _randomMessage.add(C.Player + player.getName() + C.Gray + " suffocated.");
                    break;

                case VOID:
                    _randomMessage.add(C.Player + player.getName() + C.Gray + " wanted to explore the big hole in the world!");
                    _randomMessage.add(C.Player + player.getName() + C.Gray + " thought that the black place was space.");
                    break;

                default:
                    _randomMessage.add(C.Player + player.getName() + C.Gray + " died of unknown causes.");
                    _randomMessage.add(C.Player + player.getName() + C.Gray + " died.");
                    _randomMessage.add("RIP " + C.Player + player.getName() + C.End);
                    break;
            }
        }
        else
        {
            _randomMessage.add(C.Player + player.getName() + C.Gray + " died.");
        }

        String _message;

        if (! _randomMessage.isEmpty())
            _message = CollectionUtil.randomElement(_randomMessage);
        else
            _message = C.Player + player.getName() + C.Gray + " died randomly.";

        MessageUtil.b("Death", _message);
    }

    private void announceHomicide(Player player, Player killer)
    {
        LinkedList<String> _randomKillMessage = new LinkedList<>();
        _randomKillMessage.clear();

        if (killer != null)
        {
            _randomKillMessage.add(C.Player + player.getName() + C.Body + " was killed by " + C.Player + killer.getName() + C.Body + " with " + WordUtil.formatItemName(killer.getInventory().getItemInMainHand(), true) + C.End);
            _randomKillMessage.add(C.Player + player.getName() + C.Body + " was slain by the almighty " + C.Player + killer.getName() + C.Body + " with just " + WordUtil.formatItemName(killer.getInventory().getItemInMainHand(), true) + C.End);
            _randomKillMessage.add(C.Player + player.getName() + C.Body + " was shrekt by " + C.Player + killer.getName() + C.Body + " with " + WordUtil.formatItemName(killer.getInventory().getItemInMainHand(), true) + C.End);
        }

        MessageUtil.b("Death", CollectionUtil.randomElement(_randomKillMessage));
    }

}
