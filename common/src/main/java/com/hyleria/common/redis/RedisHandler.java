package com.hyleria.common.redis;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import com.hyleria.common.redis.api.Focus;
import com.hyleria.common.redis.api.FromChannel;
import com.hyleria.common.redis.api.HandlesType;
import com.hyleria.common.redis.api.Payload;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import javax.inject.Singleton;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/24/2017 (11:57 AM)
 */
@Singleton
public class RedisHandler
{

    /** whether or not to print out debug messages here */
    private static final boolean DEBUG_ENABLED = Boolean.valueOf(System.getProperty("com.hyleria.common.redis.debug", "false"));

    /** convert raw JSON strings into Java objects */
    private static final JSONParser JSON_PARSER = new JSONParser();

    /** JSON <-> Java object */
    private static final Gson GSON = new Gson();

    /** load the focus from the provided payload class | cache due to the basic reflection present */
    private LoadingCache<Class<? extends Payload>, String> payloadFocusCache = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build(new CacheLoader<Class<? extends Payload>, String>()
    {
        @Override
        public String load(Class<? extends Payload> key) throws Exception
        {
            checkState(key.isAnnotationPresent(Focus.class), "Invalid payload! Missing Focus annotation.");
            debug("Fetching focus for payload type: " + key.getName());

            return key.getAnnotation(Focus.class).value();
        }
    });

    /** a pool of redis connections */
    private JedisPool pool;

    /** one jedis instance dedicated to the thread-blocking op of */
    private volatile Jedis subscriber;

    /** collection of hooks to our redis system */
    private ConcurrentHashMap<String, HookData> hooks;

    /** run redis requests async */
    private ExecutorService executor;

    /**
     * Start up our connection pool
     *
     * @return this handler
     */
    public RedisHandler init()
    {
        pool = new JedisPool();
        executor = Executors.newCachedThreadPool();

        return this;
    }

    /**
     * Start receiving data from the provided
     * Redis channels
     *
     * @param channels the channels to listen to
     * @return this handler
     */
    public RedisHandler subscribe(final RedisChannel... channels)
    {
        checkNotNull(pool, "use RedisHandler#connect before starting up pub/sub");

        // we need the channels we're subbing to as Strings so
        final String[] _channelsAsString = new String[channels.length];

        for (int i = 0; i < channels.length; i++)
            _channelsAsString[i] = channels[i].channel;

        new Thread("Hyleria Redis Pub/Sub")
        {
            @Override
            public void run()
            {
                subscriber = pool.getResource();
                subscriber.connect();

                subscriber.subscribe(new JedisPubSub()
                {
                    @Override
                    public void onMessage(String channel, String message)
                    {
                        try
                        {
                            debug("Received JSON message on channel: " + channel);
                            debug("Message: [" + message + "]");

                            final JSONObject _json = (JSONObject) JSON_PARSER.parse(message);
                            final String _focus = (String) _json.get("focus");

                            if (hooks.containsKey(_focus))
                            {
                                final HookData _data = hooks.get(_focus);

                                if (_data.channel.equals(channel))
                                {
                                    _data.method.invoke(_data.possessor, GSON.fromJson(((JSONObject) _json.get("payload")).toJSONString(), _data.payloadType));
                                }
                            }
                        }
                        catch (ParseException ex)
                        {
                            System.err.println("Invalid JSON provided to Redis system");
                            System.err.println("Verify this payload is correct:");
                            System.err.println(message);
                            System.err.println();
                        }
                        catch (IllegalAccessException | InvocationTargetException ex)
                        {
                            ex.printStackTrace();
                            System.err.println("Unable to manually invoke the method behind the provided Redis hook");
                            System.err.println();
                        }
                    }
                }, _channelsAsString);
            }
        };

        return this;
    }

    /**
     * Send out a payload across our
     * Redis instance(s).
     *
     * @param channel the channel to send it over
     * @param payload what we're sending
     * @return this handler
     */
    public RedisHandler publish(String channel, Payload payload)
    {
        executor.submit(() ->
        {
            debug("Publishing payload on " + channel + "\npayload w/o focus: [" + payload.asJSON().toJSONString() + "]");

            try (Jedis jedis = pool.getResource())
            {
                jedis.publish(channel, payload.asString(payloadFocusCache.get(payload.getClass())));
            }
            catch (ExecutionException ex)
            {
                ex.printStackTrace();
                System.err.println("Issue fetching focus for payload: " + payload.getClass().getName());
                System.err.println();
            }
        });

        return this;
    }

    /**
     * Take in the provided hook, and make sure
     * this handler starts to take it into
     * consideration whilst processing payloads.
     *
     * @param object what you'd like to register
     * @return this handler
     */
    public RedisHandler registerHook(Object object)
    {
        try
        {
            // lazy init
            if (hooks == null)
                hooks = new ConcurrentHashMap<>();

            boolean _provisionedHook = false;

            for (Method method : object.getClass().getMethods())
            {
                // every "virtual hook" must have one of these
                if (method.isAnnotationPresent(HandlesType.class))
                {
                    checkState(method.getParameterCount() == 1, "We only invoke with the provided payload; nothing else!");
                    checkState(method.getParameterTypes()[0].isAssignableFrom(Payload.class), "The provided parameter isn't a payload!");

                    final HookData _data = new HookData();

                    _data.possessor = object;
                    _data.method = method;
                    _data.channel = method.isAnnotationPresent(FromChannel.class) ? method.getAnnotation(FromChannel.class).value().channel : RedisChannel.DEFAULT.channel;
                    _data.focus = payloadFocusCache.get(method.getAnnotation(HandlesType.class).value());

                    hooks.put(_data.focus, _data);

                    _provisionedHook = true;
                }
            }

            // in case someone screwed up
            checkState(_provisionedHook, "A method conforming to the standards of our hooks has not been found in [" + object.getClass().getName() + "]. please fix!!");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return this;
    }

    /**
     * Prints the provided message
     * only if debug is enabled here
     *
     * @param message the message
     */
    private static void debug(String message)
    {
        if (DEBUG_ENABLED)
            System.out.println("[Redis Debug] " + message);
    }

    /**
     * Set of data pertaining to a Redis hook
     */
    private static class HookData
    {
        /** instance of the item holding this hook */
        Object possessor;

        /** Java method backing this hook */
        Method method;

        /** the redis channel we're looking for */
        String channel;

        /** the intent of the payload for this hook */
        String focus;

        /** the payload type this hook is processing */
        Class<? extends Payload> payloadType;
    }

}
