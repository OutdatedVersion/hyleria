package com.hyleria.coeus.available.uhc.world;

import com.hyleria.util.VectorUtil;
import com.sk89q.worldedit.regions.CuboidRegion;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.hyleria.util.Colors.bold;
import static com.hyleria.util.VectorUtil.fromBukkit;
import static org.bukkit.ChatColor.GREEN;
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
    private Location origin;

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

    public Border updateBorder(int newApothem)
    {
        return this;
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

    @EventHandler
    public void tp(PlayerCommandPreprocessEvent event)
    {
        if (event.getMessage().equalsIgnoreCase("/w"))
        {
            event.setCancelled(true);
            event.getPlayer().teleport(origin);
            event.getPlayer().sendMessage(bold(GREEN) + "You've been relocated.");
        }
    }

}
