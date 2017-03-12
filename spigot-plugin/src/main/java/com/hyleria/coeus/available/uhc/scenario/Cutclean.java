package com.hyleria.coeus.available.uhc.scenario;

import com.hyleria.util.BlockUtil;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

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
    private static final ItemStack FLINT = new ItemStack(Material.FLINT);

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

        final Optional<Material> _possibleItem = _drops.stream()
                                                       .map(ItemStack::getType)
                                                       .map(BlockUtil::preciousDropFromBlock)
                                                       .filter(Objects::nonNull).findFirst();

        if (_possibleItem.isPresent())
        {
            _drops.clear();
            _drops.add(new ItemStack(_possibleItem.get()));

            // remove the ability to gain experience
            event.setExpToDrop(0);
        }
    }

    @EventHandler
    public void autoCook(EntityDeathEvent event)
    {

    }

}
