package com.hyleria.commons.reflect;

import com.google.gson.annotations.SerializedName;

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


}
