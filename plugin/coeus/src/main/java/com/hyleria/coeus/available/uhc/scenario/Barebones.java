package com.hyleria.coeus.available.uhc.scenario;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/13/2017 (12:32 AM)
 */
public class Barebones extends UHCScenario
{

    /** one diamond */
    private static final ItemStack IRON = new ItemStack(Material.IRON_INGOT);

    /** one gapple */
    private static final ItemStack GAPPLE = new ItemStack(Material.GOLDEN_APPLE, 1, (byte) 1);

    // 1 diamond
    // 16 arrows
    // 3 bits of string

    @Override
    public String name()
    {
        return "Barebones";
    }

    @EventHandler
    public void handleDrops(BlockBreakEvent event)
    {
        final Block _block = event.getBlock();

        if (_block.getType() == Material.GOLD_ORE || _block.getType() == Material.DIAMOND_ORE)
        {
            _block.getDrops().clear();
            _block.getDrops().add(IRON);
        }
    }

    @EventHandler
    public void disableGappleCrafting(PrepareItemCraftEvent event)
    {
        if (event.getInventory().getResult().equals(GAPPLE))
            event.getInventory().setResult(null);
    }

    // drops

}
