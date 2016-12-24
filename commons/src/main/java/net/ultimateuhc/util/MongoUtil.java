package net.ultimateuhc.util;

import com.google.gson.annotations.SerializedName;
import org.bson.Document;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * OutdatedVersion
 * Dec/23/2016 (5:07 PM)
 */

public class MongoUtil
{

    /** allows us to check that a {@link String} is in fact an {@link UUID} */
    public static Pattern UUID_REGEX = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");

    /**
     * Uses a default value and a MongoDB
     * document to create a new instance
     * of the provided type ({@code T}).
     *
     * @param defaultValue defaults for our new one
     * @param document the document from the database
     * @param <T> type of what we're working with
     * @return the new object
     */
    public static <T> T translate(T defaultValue, Document document)
    {
        try
        {
            // an instance of T that we may mutilate (final value)
            final Object _returnValue = defaultValue.getClass().newInstance();

            // iterate over what we need to set from our default
            for (Field field : defaultValue.getClass().getDeclaredFields())
            {
                // the identifier for this field that we're going with
                // we respect custom names via an annotation from Google's GSON project
                final String _name = field.isAnnotationPresent(SerializedName.class)
                                     ? field.getAnnotation(SerializedName.class).value()
                                     : field.getName();

                // grab the value we got from the database
                Object _current = document.get(_name);

                // hacky way to handle UUIDs for now..
                if (_current instanceof String && UUID_REGEX.matcher((String) _current).matches())
                    _current = Translators.translatorFor(UUID.class, String.class).read((String) _current);

                // set the value on the new object
                // they're guaranteed to be the same type, so no issues there
                final Field _toUpdate = _returnValue.getClass().getField(_name);

                // most of the fields are private so
                // we need to forcefully change them
                _toUpdate.setAccessible(true);

                _toUpdate.set(_returnValue, _current == null
                                            ? field.get(defaultValue)
                                            : _current);
            }

            return (T) _returnValue;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return null;
    }

}
