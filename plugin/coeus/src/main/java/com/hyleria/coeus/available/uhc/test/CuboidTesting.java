package com.hyleria.coeus.available.uhc.test;

import com.hyleria.command.api.Command;
import com.hyleria.command.api.SubCommand;
import com.hyleria.command.api.annotation.Permission;
import com.hyleria.common.reference.Role;
import com.hyleria.util.VectorUtil;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.CuboidRegion;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/25/2017 (11:21 PM)
 */
public class CuboidTesting
{

    private Vector center;
    private CuboidRegion region = CuboidRegion.fromCenter(new Vector(0, 0, 0), 1);
    private World world;

    @Command ( executor = "cube" )
    @Permission ( Role.DEV )
    public void controlCube(Player player)
    {
        player.sendMessage("Use /cube create <distance> then /cube border");
    }

    @SubCommand ( of = "cube", executors = "create" )
    public void create(Player player, String distance)
    {
        world = player.getWorld();

        int _distance = Integer.parseInt(distance);

        center = VectorUtil.fromBukkit(player.getLocation().toVector());

        // TODO(Ben): implement AWE integration
        // pos Z, neg x
        final Vector _minPoint = new Vector(center).add(0, 0, _distance).subtract(_distance, center.getBlockY(), 0);
        final Vector _maxPoint = new Vector(center).add(_distance, 255 - center.getBlockY(), 0).subtract(0, 0, _distance);

        region.setPos1(_minPoint);
        region.setPos2(_maxPoint);

        world.getBlockAt(center.getBlockX(), center.getBlockY(), center.getBlockZ()).setType(Material.REDSTONE_BLOCK);
        world.getBlockAt(_minPoint.getBlockX(), _minPoint.getBlockY(), _minPoint.getBlockZ()).setType(Material.MELON_BLOCK);
        world.getBlockAt(_maxPoint.getBlockX(), _maxPoint.getBlockY(), _maxPoint.getBlockZ()).setType(Material.COAL_BLOCK);

        player.sendMessage(ChatColor.GREEN + "Created cube figure");
        player.sendMessage(ChatColor.GRAY + "Center: " + ChatColor.YELLOW + center.toString());
        player.sendMessage(ChatColor.GRAY + "Min [Pos 1]: " + ChatColor.YELLOW + region.getMinimumPoint().toString());
        player.sendMessage(ChatColor.GRAY + "Max [Pos 2]: " + ChatColor.YELLOW + region.getMaximumPoint().toString());
        player.sendMessage(ChatColor.GRAY + "Dimensions: " + ChatColor.YELLOW + region.getLength() + 'x' + region.getWidth());
    }

    @SubCommand ( of = "cube", executors = "gen" )
    public void border(Player player)
    {
        player.sendMessage(ChatColor.YELLOW + "Generating border..");

        region.getFaces().iterator().forEachRemaining(vec ->
        {
            final Block _block = world.getBlockAt(vec.getBlockX(), vec.getBlockY(), vec.getBlockZ());
            _block.setType(Material.COBBLESTONE);
        });
    }

    @SubCommand ( of = "cube", executors = "air" )
    public void reset(Player player)
    {
        player.sendMessage(ChatColor.YELLOW + "Removing border");

        region.getFaces().iterator().forEachRemaining(vec ->
        {
            final Block _block = world.getBlockAt(vec.getBlockX(), vec.getBlockY(), vec.getBlockZ());
            _block.setType(Material.AIR);
        });
    }

}
