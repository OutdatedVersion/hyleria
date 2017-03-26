package com.hyleria.backend;

import com.google.common.collect.Queues;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hyleria.backend.data.ConnectedServer;
import com.hyleria.common.inject.StartParallel;

import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/13/2017 (4:48 PM)
 */
@Singleton
@StartParallel
public class ServerCreator implements Runnable
{

    /** */
    @Inject private ServerManager serverManager;

    /** */
    private Queue<CreationRequest> taskQueue = Queues.newConcurrentLinkedQueue();

    /** */
    private ExecutorService executor = Executors.newFixedThreadPool(8);

    /**
     *
     * @param requestedBy
     * @param serverType
     * @return
     */
    public CreationRequest fileRequest(String requestedBy, String serverType)
    {
        final CreationRequest _request = new CreationRequest(requestedBy, serverType);
        taskQueue.add(_request);

        return _request;
    }

    /**
     *
     * @param request
     * @return
     */
    public Future<ConnectedServer> create(final CreationRequest request)
    {
        // reserve an ID/name
        // find a directory
        // copy the template
        // run the startup script
        // wait for ping
        // send data in

        return executor.submit(() ->
        {
            return null;
        });
    }

    @Override
    public void run()
    {
        if (!taskQueue.isEmpty())
            create(taskQueue.poll());
    }

    /**
     *
     */
    public static class CreationRequest
    {
        String requestedBy;
        String requestedServerType;
        long filedAt = System.currentTimeMillis();

        protected CreationRequest(String requestedBy, String requestedServerType)
        {
            this.requestedBy = requestedBy;
            this.requestedServerType = requestedServerType;
        }
    }

}
