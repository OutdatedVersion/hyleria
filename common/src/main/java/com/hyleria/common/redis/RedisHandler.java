package com.hyleria.common.redis;

import com.hyleria.common.collection.MapWrapper;
import com.hyleria.common.redis.api.Focus;
import com.hyleria.common.redis.api.FromChannel;
import com.hyleria.common.redis.api.Payload;
import com.hyleria.common.redis.api.RedisHook;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/24/2017 (11:57 AM)
 */
public class RedisHandler
{

    /** convert raw JSON strings into Java objects */
    private static final JSONParser JSON_PARSER = new JSONParser();

    /** a pool of redis connections */
    private JedisPool pool;

    /** one jedis instance dedicated to the thread-blocking op of */
    private volatile Jedis subscriber;

    /** collection of hooks to our redis system */
    private ConcurrentHashMap<String, HookData> hooks;

    /**
     * Start up our connection pool
     *
     * @return this handler
     */
    public RedisHandler init()
    {
        pool = new JedisPool();
        return this;
    }

    /**
     * Start receiving data from the provided
     * Redis channels
     *
     * @param channels the channels to listen to
     * @return this handler
     */
    public RedisHandler subscribe(final String... channels)
    {
        checkNotNull(pool, "use #connect before starting up pub/sub");

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
                            final JSONObject _json = (JSONObject) JSON_PARSER.parse(message);
                            final String _focus = (String) _json.get("focus");

                            if (hooks.containsKey(_focus))
                            {
                                final HookData _data = hooks.get(_focus);

                                if (_data.channel.equals(channel))
                                    _data.hook.handleFromRedis(MapWrapper.fromJSON((JSONObject) _json.get("payload")));
                            }
                        }
                        catch (ParseException ex)
                        {
                            System.err.println("Invalid JSON provided to Redis system");
                            System.err.println("Verify this payload is correct:");
                            System.err.println(message);
                            System.err.println();
                        }
                    }
                }, channels);
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
        try (Jedis jedis = pool.getResource())
        {
            jedis.publish(channel, payload.asString());
        }

        return this;
    }

    /**
     * Take in the provided hook, and make sure
     * this handler starts to take it into
     * consideration whilst processing payloads.
     *
     * @param hook what you'd like to register
     * @return this handler
     */
    public RedisHandler registerHook(RedisHook hook)
    {
        boolean _provisionedHook = false;

        for (Method method : hook.getClass().getMethods())
        {
            // every single RedisHook must have
            if (method.isAnnotationPresent(Focus.class))
            {
                final HookData _data = new HookData();

                _data.hook = hook;
                _data.channel = method.isAnnotationPresent(FromChannel.class)
                                ? method.getAnnotation(FromChannel.class).value()
                                : RedisChannels.DEFAULT;

                hooks.put(method.getAnnotation(Focus.class).value(), _data);

                _provisionedHook = true;

                // only one per class, let's not waste our time
                break;
            }
        }

        // in case someone screwed up
        checkState(_provisionedHook, "An annotation conforming to the standards of our hooks has not been found in [" + hook.getClass().getName() + "]. please fix!!");

        return this;
    }

    /**
     * Set of data pertaining to a {@link RedisHook}
     */
    private static class HookData
    {
        /** Java method backing this hook */
        RedisHook hook;

        /** the redis channel we're looking for */
        String channel;
    }

}
