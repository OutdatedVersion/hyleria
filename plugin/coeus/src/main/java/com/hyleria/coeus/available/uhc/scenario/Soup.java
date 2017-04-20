package com.hyleria.coeus.available.uhc.scenario;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/11/2017 (8:51 PM)
 */
public class Soup extends UHCScenario
{

    @Override
    public String name()
    {
        return "Soup";
    }

    @EventHandler
    public void handle(PlayerInteractEvent event)
    {
        if (event.getMaterial() == Material.MUSHROOM_SOUP)
        {
            final Player _player = event.getPlayer();

            // regen health - may need to fine tune the length/intensity of this
            _player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 4, 5, true));

            // consider playing some effect & sound

            _player.setItemInHand(null);
            event.setCancelled(true);
        }
    }

}
