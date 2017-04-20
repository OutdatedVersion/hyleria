package com.hyleria.common.reflect;

import com.google.gson.annotations.SerializedName;
import com.hyleria.common.json.Exclude;
import org.json.simple.JSONObject;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
  * @author Ben (OutdatedVersion)
  * @since Dec/25/2016 (12:32 AM)
  */
public class ReflectionUtil
{

    /** a list containing all of Java's primitive type classes */
    public static List<Class<?>> PRIMITIVE_TYPES = Arrays.asList(boolean.class, byte.class, char.class, short.class, int.class, long.class, float.class, double.class, void.class);

    /**
     * Checks if the provided {@link Field}'s type
     * is one of Java's primitives
     *
     * @param field the field
     * @return yes it is, or no it is not
     */
    public static boolean isPrimitive(Field field)
    {
        return PRIMITIVE_TYPES.contains(field.getType());
    }

    /**
     * Grabs the "human friendly" name of
     * the specified field.
     *
     * @param field the field
     * @return the name
     */
    public static String nameFromField(Field field)
    {
        return field.isAnnotationPresent(SerializedName.class)
                ? field.getAnnotation(SerializedName.class).value()
                : field.getName();
    }

    /**
     * Whether or not we should continue going
     * whilst iterating the fields in a class
     * during reflection.
     *
     * @param field the field
     * @return yes or no
     */
    public static boolean skipOver(Field field)
    {
        return field.isAnnotationPresent(Exclude.class);
    }

    /**
     * Load a class via the name of it
     *
     * @param name the fully qualified name for this class
     * @return the class
     */
    public static Class<?> classForName(String name)
    {
        try
        {
            return Class.forName(name);
        }
        catch (ClassNotFoundException ex)
        {
            ex.printStackTrace();
            System.err.println("Unable to find class at location: [" + name + "]");

            return null;
        }
    }

    /**
     * Turn the provided object into
     * a JSON object w/ the contents
     * of every field in the object.
     *
     * <p>
     *     No guarantees on how well this
     *     performs. Only meant for testing!
     * </p>
     *
     * @param object the object
     */
    public static String printOut(Object object)
    {
        final JSONObject _json = new JSONObject();

        // iterate over each field, and insert it
        for (Field field : object.getClass().getDeclaredFields())
        {
            field.setAccessible(true);

            try
            {
                _json.put(field.getName(), field.get(object).toString());
            }
            catch (IllegalAccessException ex)
            {
                ex.printStackTrace();
            }
        }


        final String _jsonString = _json.toJSONString();

        System.out.println();
        System.out.println(_jsonString);
        System.out.println();

        return _jsonString;
    }

}
