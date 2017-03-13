package com.hyleria.coeus.available.uhc.scenario;

import com.google.common.collect.Lists;
import com.hyleria.commons.math.Math;
import com.hyleria.util.Colors;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.BiFunction;

import static java.lang.String.format;
import static org.bukkit.ChatColor.RED;

/**
 * Team Based >> Yourself and a
 * partner have a virtual 'backpack'
 * to store your items in. In the case
 * that both of you die this 'chest'
 * is destroyed; so nothing will drop.
 *
 * @author Ben (OutdatedVersion)
 * @since Mar/13/2017 (12:07 AM)
 */
public class Backpacks extends UHCScenario
{

    /** the size of the inventory behind the backpacks (three row) */
    private static final int PACK_SIZE = Math.inventorySizeFromRows(3);

    /** a function that takes in two players (the pack holders) and create's the name of an inventory from them */
    private static final BiFunction<Player, Player, String> PACK_NAME_CREATOR =
            (holder, partner) -> Colors.bold(RED) + format("\uD83C\uDF92 Backpack » %s & %s", holder.getName(), partner.getName());

    /** the bag */
    // TODO(Ben): need the builder
    private static final ItemStack BAG = new ItemStack(Material.LEATHER);

    /** a collection of all current packs */
    private List<Backpack> backpacks = Lists.newArrayList();

    @Override
    public String name()
    {
        return "Backpacks";
    }

    /**
     * Creates a pack using the two
     * players as its holders
     *
     * @param holder 1st player
     * @param partner 2nd player
     * @return the fresh pack
     */
    public Backpack create(Player holder, Player partner)
    {
        final Backpack _pack = new Backpack(holder, partner);
        backpacks.add(_pack);

        // give the player something to open the bag

        return _pack;
    }

    /**
     * Represents a theoretical backpack
     * within the game
     */
    public static class Backpack
    {
        private Player holder, partner;
        private Inventory inventory;

        public Backpack(Player holder, Player partner)
        {
            this.holder = holder;
            this.partner = partner;

            this.inventory = Bukkit.createInventory(null, PACK_SIZE, PACK_NAME_CREATOR.apply(holder, partner));
        }
    }

}
