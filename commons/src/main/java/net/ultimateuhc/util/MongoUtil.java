package net.ultimateuhc.util;

import com.google.common.base.Preconditions;
import com.google.gson.annotations.SerializedName;
import com.simplexitymc.util.json.Exclude;
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

    /** what are we doing? */
    enum Action
    {
        READ_FROM_DOCUMENT,
        WRITE_TO_DOCUMENT
    }

    /**
     * Turn the provided value into a document
     * based on transgressions between the default
     * value & working value.
     *
     * @param value the value that has content
     * @param defaultValue all of the defaults for that
     * @param document the mongo document we're writing to
     * @param <T> the type of these values
     * @return a written document
     */
    public static <T, R> R write(Class<R> returnType, T value, T defaultValue, Document document)
    {
        return translateInternal(Action.WRITE_TO_DOCUMENT, returnType, value, defaultValue, document);
    }

    /**
     * Takes in data from a Mongo document
     *
     * @param returnType the type that we're working with
     * @param value working value
     * @param defaultValue value w/ defaults
     * @param document mongo doc
     * @param <T> type
     * @param <R> return type
     * @return finished document
     */
    public static <T, R> R read(Class<R> returnType, T value, T defaultValue, Document document)
    {
        return translateInternal(Action.READ_FROM_DOCUMENT, returnType, value, defaultValue, document);
    }

    /**
     * Uses a default value and a MongoDB
     * document to create a new instance
     * of the provided type ({@code T}).
     *
     * @param action what are we doing?
     *               READ: we're grabbing values from
     *               the provided {@code value}, and
     *               writing it to an object of the same
     *               type injecting default values.
     *               WRITE: we're inserting data into
     *               the Mongo document so we may send
     *               it off to our Mongo instance
     * @param value defaults for our new one
     * @param defaultValueHolder in case our action is {@link Action#WRITE_TO_DOCUMENT}
     *                           we'll need something to compare default values to
     * @param document the document from the database
     * @param <T> type of what we're working with
     * @return the new object
     */
    private static <T, R> R translateInternal(Action action, Class<R> returnType, T value, T defaultValueHolder, Document document)
    {
        try
        {
            Preconditions.checkNotNull(defaultValueHolder, "We need a default value to work with!");

            // an instance of T that we may mutilate (final value)
            final Object _returnValue = action == Action.READ_FROM_DOCUMENT ? value.getClass().newInstance() : null;

            // iterate over what we need to set from our default
            for (Field field : value.getClass().getDeclaredFields())
            {
                // in the case that something shouldn't be included
                // this annotation will be present - let's honor it.
                if (field.isAnnotationPresent(Exclude.class))
                    continue;

                // the identifier for this field that we're going with
                // we respect custom names via an annotation from Google's GSON project
                final String _name = field.isAnnotationPresent(SerializedName.class)
                                     ? field.getAnnotation(SerializedName.class).value()
                                     : field.getName();


                if (action == Action.READ_FROM_DOCUMENT)
                {
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
                    // butttt check just in-case..
                    if (!_toUpdate.isAccessible())
                        _toUpdate.setAccessible(true);

                    _toUpdate.set(_returnValue, _current == null
                                                ? field.get(value)
                                                : _current);
                }
                else
                {
                    // grab the default value of this field from
                    // our default value class
                    final Object _defaultValue = defaultValueHolder.getClass().getField(field.getName()).get(defaultValueHolder);

                    // write different value
                    if (field.get(value) != _defaultValue)
                    {
                        if (ReflectionUtil.isPrimitive(field))
                            document.put(_name, field.get(field));
                        else if (value.getClass().isAssignableFrom(DocumentCompatible.class))
                            document.put(_name, ((DocumentCompatible) value).asDocument());
                        else
                            throw new IllegalArgumentException("We couldn't convert [" + value.getClass().getSimpleName() + "] to a Document");
                    }
                }
            }

            // idk. all of this is just so dirty.
            return _returnValue == null ? (R) document : (R) _returnValue;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return null;
    }

}
