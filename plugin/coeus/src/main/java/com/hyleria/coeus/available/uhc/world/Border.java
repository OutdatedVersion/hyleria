package com.hyleria.coeus.available.uhc.world;

import com.hyleria.util.VectorUtil;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.CuboidRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.hyleria.util.Colors.bold;
import static org.bukkit.ChatColor.RED;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/15/2017 (10:25 PM)
 */
public class Border implements Listener
{

    /** the world that this border is in */
    private World world;

    /** the center of our region */
    private Vector center = new Vector();

    /** two end points of our region */
    private Vector minPoint, maxPoint;

    /** the content of this border's interior */
    private CuboidRegion region = CuboidRegion.fromCenter(center, 1);

    /** what our border is made out of */
    private Material borderMaterial = Material.BEDROCK;

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
     * @param distance the size of the border
     * @param center the center of the border
     * @return this border
     */
    public Border init(int distance, Location center)
    {
        this.world = center.getWorld();
        this.center = VectorUtil.fromBukkit(center.toVector());

        this.minPoint = new Vector(this.center).add(0, 0, distance).subtract(distance, center.getBlockY(), 0);
        this.maxPoint = new Vector(this.center).add(distance, 200 - center.getBlockY(), 0).subtract(0, 0, distance);

        region.setPos1(minPoint);
        region.setPos2(maxPoint);

        System.out.println("[UHC] Border initialized (" + region.getWidth() + 'x' + region.getLength() + ')');

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
        checkNotNull(region, "The #init method must be invoked before anything else")
                .getWalls().iterator().forEachRemaining(vec ->
        {
            // height check?
            final Block _block = world.getBlockAt(vec.getBlockX(), vec.getBlockY(), vec.getBlockZ());
            _block.setType(borderMaterial);

            // once again, this was the first (quick) implementation of this
            // wayyy better ways to do this. to start, AsyncWorldEdit would
            // be a great idea. just getting their API kind of sucks..
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
        // I removed this awhile back

        return this;
    }

    /**
     * @return the region backing this border
     */
    public CuboidRegion region()
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
