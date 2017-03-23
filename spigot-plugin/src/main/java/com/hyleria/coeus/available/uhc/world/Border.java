package com.hyleria.coeus.available.uhc.world;

import com.hyleria.util.VectorUtil;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.hyleria.util.Colors.bold;
import static com.hyleria.util.VectorUtil.fromBukkit;
import static org.bukkit.ChatColor.RED;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/15/2017 (10:25 PM)
 */
public class Border implements Listener
{

    /** the size (one way) of the region */
    private int apothem;

    /** the world that this border is in */
    private World world;

    /** the center of our region */
    public Location origin;

    /** the content of this border's interior */
    private CuboidRegion region;

    /** what our border is made out of */
    private Material borderMaterial = Material.BEDROCK;

    // create the physical border
    // keep players from exiting it

    /**
     * Creates a border, but from default values.
     * The very center of a world named "uhc"
     *
     * @param apothem the size of the border
     * @return this border
     */
    public Border init(int apothem)
    {
        // may need to do this ourselves; we don't count trees & such.
        return init(apothem, Bukkit.getWorld("uhc").getHighestBlockAt(0, 0).getLocation());
    }

    /**
     * Sets the values for this, and loads
     * up the region backing this border.
     *
     * @param apothem the size of the border
     * @param center the center of the border
     * @return this border
     */
    public Border init(int apothem, Location center)
    {
        this.apothem = apothem;
        this.origin = center;
        this.world = center.getWorld();

        region = CuboidRegion.fromCenter(fromBukkit(center.toVector()), apothem);

        return this;
    }

    public Border loadChunks()
    {
        checkNotNull(region, "call #init first");

        long _startedAt = System.currentTimeMillis();
        region.getChunks().forEach(vec -> world.getChunkAtAsync(vec.getBlockX(), vec.getBlockZ(), Chunk::load));
        System.out.println("TIME: " + (System.currentTimeMillis() - _startedAt));

        return this;
    }

    /**
     * Create the actual border around this
     * "virtual" border.
     *
     * @return this border
     */
    public Border generatePhysicalBorder()
    {
        checkNotNull(region, "The #init method must be invoked before anything else");

        region.getWalls().iterator().forEachRemaining(vec ->
        {
            // height check?

            final Block _block = world.getBlockAt(vec.getBlockX(), vec.getBlockY(), vec.getBlockZ());
            _block.setType(Material.BEDROCK);
        });

        return this;
    }

    /**
     * Shrink the border around the origin
     * by the provided amount
     *
     * @param shrinkFactor the amount to shrink by
     * @return this border
     */
    public Border shrink(int shrinkFactor)
    {
        System.out.println("pre shrink: " + this.apothem);
        init((this.apothem * 2) - shrinkFactor).generatePhysicalBorder();

        return this;
    }

    /**
     * @return the region backing this border
     */
    public Region region()
    {
        return region;
    }

    @EventHandler
    public void disallowPass(PlayerMoveEvent event)
    {
        // eye change check?

        if (!event.getPlayer().getWorld().equals(world))
            return;

        if (!region.contains(VectorUtil.fromBukkit(event.getTo().toVector())))
        {
            // event.setCancelled(true);
            event.getPlayer().sendMessage(bold(RED) + "You really shouldn't be leaving that border. :/");
        }
    }

}
