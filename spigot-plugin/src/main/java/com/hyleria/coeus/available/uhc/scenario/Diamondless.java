package com.hyleria.coeus.available.uhc.scenario;

import com.hyleria.coeus.damage.CombatDeathEvent;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Diamond ore no longer drops diamonds,
 * and players (when killed by another
 * player) drop a diamond.
 *
 * @author Ben (OutdatedVersion)
 * @since Mar/13/2017 (12:06 AM)
 */
public class Diamondless extends UHCScenario
{

    /** one diamond */
    private static final ItemStack DIAMOND = new ItemStack(Material.DIAMOND);

    @Override
    public String name()
    {
        return "Diamondless";
    }

    @EventHandler
    public void removeDrops(BlockBreakEvent event)
    {
        if (event.getBlock().getType() == Material.DIAMOND_ORE)
            event.getBlock().getDrops().clear();
    }

    @EventHandler
    public void dropDiamond(CombatDeathEvent event)
    {
        event.by().getWorld().dropItem(event.victim().getLocation(), DIAMOND);
    }

}
