package com.hyleria.coeus.available.uhc.scenario;

import com.hyleria.coeus.damage.CombatDeathEvent;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

/**
 * The ability to mine gold pieces
 * is stripped, and you may only
 * obtain them via killing a player;
 * which now drops one!
 *
 * @author Ben (OutdatedVersion)
 * @since Mar/13/2017 (12:05 AM)
 */
public class Goldless extends UHCScenario
{

    /** one gold ingot */
    private static final ItemStack GOLD = new ItemStack(Material.GOLD_INGOT);

    @Override
    public String name()
    {
        return "Goldless";
    }

    @EventHandler
    public void removeDrops(BlockBreakEvent event)
    {
        if (event.getBlock().getType() == Material.GOLD_ORE)
            event.getBlock().getDrops().clear();
    }

    @EventHandler
    public void dropDiamond(CombatDeathEvent event)
    {
        event.by().getWorld().dropItem(event.victim().getLocation(), GOLD);
    }

}
