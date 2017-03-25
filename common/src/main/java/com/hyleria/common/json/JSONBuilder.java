package com.hyleria.common.json;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.UUID;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/25/2017 (2:58 PM)
 */
public class JSONBuilder
{


    /** where we throw all the data we insert in this builder */
    private JSONObject base;

    /**
     * Starts up a fresh builder
     */
    private JSONBuilder()
    {
        base = new JSONObject();
    }

    /**
     * @return a fresh builder
     */
    public static JSONBuilder builder()
    {
        return new JSONBuilder();
    }

    /**
     * Inserts a new item into
     * our backing JSON object
     *
     * @param key where we store the value
     * @param values a chunk of things you'd like to store.
     * @return this builder
     */
    public JSONBuilder add(String key, Object... values)
    {
        if (values.length < 0)
            throw new IllegalArgumentException("You must supply at least one value");

        if (values.length > 1)
        {
            JSONArray _array = new JSONArray();
            _array.addAll(Arrays.asList(values));

            base.put(key, _array);
        }
        else
        {
            if (values[0] instanceof UUID)
                values[0] = values[0].toString();

            base.put(key, values[0]);
        }

        return this;
    }

    /**
     * @return this builder as a {@link JSONObject}
     */
    public JSONObject asJSON()
    {
        return base;
    }

    /**
     * @return this builder as JSON
     */
    public String asString()
    {
        return base.toJSONString();
    }

    @Override
    public String toString()
    {
        return asString();
    }

}
