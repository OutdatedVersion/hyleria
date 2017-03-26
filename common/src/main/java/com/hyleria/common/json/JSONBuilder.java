package com.hyleria.common.json;

import com.hyleria.common.reflect.ReflectionUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.lang.reflect.Field;
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

            if (values[0] instanceof Enum)
                values[0] = ((Enum) values[0]).name();

            base.put(key, values[0]);
        }

        return this;
    }

    /**
     * Adds the value of every field in
     * in this object to our JSON
     *
     * @param object the object
     * @return this builder
     */
    public JSONBuilder addAllFields(Object object)
    {
        for (Field field : object.getClass().getFields())
        {
            // respect exclusion annotation
            if (ReflectionUtil.skipOver(field))
                continue;

            try
            {
                add(ReflectionUtil.nameFromField(field), field.get(object));
            }
            catch (IllegalAccessException ex)
            {
                ex.printStackTrace();
                System.err.println("Unable to get field value");
                System.err.println();
            }
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
