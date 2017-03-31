package com.hyleria.coeus.available.uhc.world;

import com.google.common.collect.Queues;
import com.sk89q.worldedit.regions.CuboidRegion;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/30/2017 (7:05 PM)
 */
public class UHCLoadingTask implements Runnable
{

//    private static final BiFunction<Vector2D, World, Chunk> VECTOR_TO_CHUNK = (vec, world) -> world.getChunkAtAsync();

    /** how long to wait before moving onto the next chunk | in ticks */
    private static final int DELAY = 1;

    private static final long TIMEOUT_TRESHHOLD = TimeUnit.MILLISECONDS.toNanos(10);

    private World world;
    private Queue<Chunk> chunksToLoad;

    public UHCLoadingTask(World world, CuboidRegion region)
    {
        this.world = world;
        this.chunksToLoad = Queues.newArrayDeque();
    }

    @Override
    public void run()
    {

    }

}
