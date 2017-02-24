package com.hyleria.commons.collection;

import com.simplexitymc.util.json.JSON;
import com.simplexitymc.util.json.JSONWrapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.Map;
import java.util.UUID;

/**
 * @author Ben (OutdatedVersion)
 * @since Feb/24/2017 (11:40 AM)
 */
public class MapWrapper
{

    /** convert JSON to a POJO */
    private static final JSONParser JSON_PARSER = new JSONParser();

    /** that map we're fetching data from */
    private Map wrappedMap;

    private MapWrapper(Map wraps)
    {
        wrappedMap = wraps;
    }

    /**
     * Creates an instance of this wrapper
     * using the provided {@link Map} as
     * this map.
     *
     * @param map the map being wrapped
     * @return a new wrapper
     */
    public static MapWrapper fromMap(Map map)
    {
        return new MapWrapper(map);
    }

    /**
     * Returns an instance of a
     * wrapper that is wrapping
     * the supplied {@link JSONObject}.
     * After the wrapping all of
     * the below shortcut methods may
     * be used for accessing data
     * within the wrapped object.
     *
     * @param json The {@link JSONObject} to wrap
     *
     * @return A new {@link JSONWrapper} tied to the supplied object
     */
    public static MapWrapper fromJSON(JSONObject json)
    {
        return new MapWrapper(json);
    }

    /**
     * Returns an instance of
     * a wrapper that is wrapping
     * a freshly parsed JSON string.
     * Keep in mind that the parsing
     * may fail, so {@code null} may
     * be presented to you instead of
     * the wrapper mentioned earlier.
     *
     * @param json The JSON string to be parsed
     *
     * @return A new wrapper or {@code null}
     */
    public static MapWrapper fromJSON(String json)
    {
        try
        {
            return new MapWrapper((JSONObject) JSON_PARSER.parse(json));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            System.err.println("=====================================");
            System.err.println("Invalid JSON provided to our wrapper");
        }

        return null;
    }

    /**
     * Returns an object that
     * was received as a JSON
     * string. We will then use
     * GSON to unwrap this string,
     * and map it to a new instance of
     * the supplied class.
     *
     * @param key   The key that was stored at creation
     * @param clazz The class to deserialize the JSON as
     *
     * @return A new instance of the found JSON
     */
    public <T> T fromJSON(String key, Class<T> clazz)
    {
        return JSON.GSON.fromJson(findString(key), clazz);
    }

    /**
     * Returns a {@code String} that was
     * found within the raw JSON.
     *
     * @param key The key that was stored at creation
     *
     * @return A String casted from the raw object we found
     */
    public String findString(String key)
    {
        return (String) get(key);
    }

    /**
     * Returns an {@code int} that
     * was found within the raw JSON.
     *
     * @param key The key that was stored at creation
     *
     * @return An int casted from the raw object we retrieved
     */
    public int findInt(String key)
    {
        return (int) get(key);
    }

    /**
     * Returns a {@code long} that
     * was found within the raw JSON.
     *
     * @param key The key that was stored at creation
     *
     * @return A long casted from the raw object we retrieved
     */
    public long findLong(String key)
    {
        return (long) get(key);
    }

    /**
     * Returns a {@code boolean} that
     * was found within the raw JSON.
     *
     * @param key The key that was stored at creation
     *
     * @return A boolean casted from the raw object we retrieved
     */
    public boolean findBoolean(String key)
    {
        return (boolean) get(key);
    }

    /**
     * Makes an attempt at retrieving a UUID
     * from the JSON.
     *
     * <p>Note: This works by casting the found
     * key to a String, and then passing that in
     * as the argument to the {@link UUID#fromString(String)}
     * method. You should be positive that it was
     * stored correctly when constructing the JSON.</p>
     *
     * @param key The key that was used at creation
     *
     * @return A UUID from the provided String
     */
    public UUID findUUID(String key)
    {
        return UUID.fromString(findString(key));
    }

    /**
     * Grabs an object from the type
     * provided as a class.
     *
     * <p>
     * Keep in mind that you may also attempt
     * matching enumerators via this method. If
     * the supplied parameter: clazz is of an
     * enumeration type then we will attempt
     * to grab from our JSON as a {@link String},
     * and then attempting to use a sort of
     * {@link Enum#valueOf(Class, String)} to convert from this string
     * to an object of that enum's type.
     * </p>
     *
     * @param key   The key that was stored at creation
     * @param clazz The type to cast to
     *
     * @return The found object. {@code null} if it does not exist
     */
    public <T> T find(String key, Class<T> clazz)
    {
        // We have to do enums specially as we
        // can't just cast raw objects to enums.
        if (clazz.isEnum())
        {
            // We'll assume they stored
            // the string constant; (Enum#name)

            String _enumConstant = (String) get(key);

            for (T constant : clazz.getEnumConstants())
                if (constant.toString().equalsIgnoreCase(_enumConstant))
                    return constant;

            return null;
        }

        return (T) get(key);
    }

    /**
     * Checks to see if this
     * wrapper contains an
     * object against the
     * provided key.
     *
     * @param key Something to identify the stored object
     *
     * @return Does the wrapper contain something stored with the key parameter.
     */
    public boolean doesContain(String key)
    {
        return get(key) != null;
    }

    /**
     * The object that we retrieved
     * within the JSON string.
     *
     * @param key The key that was stored at creation
     *
     * @return The raw object
     */
    public Object get(Object key)
    {
        return wrappedMap.get(key);
    }

    /**
     * Turn this wrapper into a {@link String}.
     */
    @Override
    public String toString()
    {
        return String.format("MapWrapper(size=%s)", wrappedMap.size());
    }

    /**
     * Internal method for checking for matches
     * in a few of these methods.
     *
     * @param array the array we're looking through
     * @param matches what we're looking for
     * @return whether or not the array contains one
     *         of the {@link String}s we're searching
     *         for. (case insensitive)
     */
    private static boolean contains(String[] array, String... matches)
    {
        for (String string : array)
            for (String possibleMatch : matches)
                if (string.equalsIgnoreCase(possibleMatch))
                    return true;

        return false;
    }

}
