package com.hyleria.coeus.available.uhc.scenario;

import com.hyleria.coeus.damage.CombatDeathEvent;
import com.hyleria.common.time.Time;
import com.hyleria.util.Scheduler;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/15/2017 (1:08 AM)
 */
public class Timebomb extends UHCScenario
{

    /** the size of the explosion */
    private static final int EXPLOSION_DIAMETER = 6;

    /** one golden head */
    private static final ItemStack GOLDEN_HEAD = new ItemStack(Material.SKULL, 1, (byte) 3);

    @Override
    public String name()
    {
        return "Timebomb";
    }

    @EventHandler
    public void createChest(CombatDeathEvent event)
    {
        final Block _block = event.by().getLocation().getBlock();

        _block.setType(Material.CHEST);
        Chest _chest = (Chest) _block.getState();

        _chest.getBlockInventory().addItem(GOLDEN_HEAD);
        _chest.getBlockInventory().addItem(event.by().getInventory().getContents());

        Scheduler.delayed(() ->
        {
            _chest.setType(Material.AIR);
            _chest.getWorld().createExplosion(_chest.getLocation(), EXPLOSION_DIAMETER);
            // may need to adjust the size of this explosion
        }, Time.SECONDS.toTicks(30));
    }

}
