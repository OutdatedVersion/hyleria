package com.hyleria.coeus.available.uhc.scenario;

import com.google.common.collect.Lists;
import com.hyleria.common.math.Math;
import com.hyleria.util.BlockUtil;
import com.hyleria.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Objects;

/**
 * Anything that drops a smeltable/cookable
 * item has already had that done to it. For
 * example, when ire ore is mined it will
 * drop an iron ingot. Additionally, flint
 * has a definite chance of dropping & apples
 * have a 1.25% chance of dropping from severed
 * trees
 *
 * @author Ben (OutdatedVersion)
 * @since Mar/11/2017 (7:41 PM)
 */
public class Cutclean extends UHCScenario
{

    /** one flint */
    static final ItemStack FLINT = new ItemStack(Material.FLINT);

    /** one apple */
    static final ItemStack APPLE = new ItemStack(Material.APPLE);

    @Override
    public String name()
    {
        return "Cutclean";
    }

    @EventHandler
    public void autoSmelt(BlockBreakEvent event)
    {
        final Collection<ItemStack> _drops = event.getBlock().getDrops();

        // guarantee that flint will drop
        if (event.getBlock().getType() == Material.GRAVEL)
        {
            _drops.clear();
            _drops.add(FLINT.clone());
            return;
        }

        _drops.stream()
                .map(ItemStack::getType)
                .map(BlockUtil::preciousDropFromBlock)
                .filter(Objects::nonNull)
                .findFirst()
                .ifPresent(possibleItem ->
                {
                    _drops.clear();
                    _drops.add(new ItemStack(possibleItem));

                    // remove the ability to gain experience
                    event.setExpToDrop(0);
                });
    }

    @EventHandler
    public void autoCook(EntityDeathEvent event)
    {
        final Collection<ItemStack> dropClone = Lists.newArrayList(event.getDrops());
        event.getDrops().clear();
        dropClone.stream().map(ItemStack::getType).map(ItemUtil::cookedItemFor).filter(Objects::nonNull).forEach(drop -> event.getDrops().add(new ItemStack(drop)));
    }

    @EventHandler
    public void leaves(LeavesDecayEvent event)
    {
        event.getBlock().getDrops().clear();

        if (Math.chance(1.25))
            event.getBlock().getDrops().add(APPLE);
    }

}
