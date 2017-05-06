package com.hyleria.common.mongo;

import com.google.gson.annotations.SerializedName;
import com.hyleria.common.json.Exclude;
import com.hyleria.common.reflect.ReflectionUtil;
import org.bson.Document;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkState;

/**
 * @author Ben (OutdatedVersion)
 * @since Dec/23/2016 (5:07 PM)
 */
@Deprecated
public class MongoUtil
{

    /** what are we doing? */
    enum Action
    {
        READ_FROM_DOCUMENT,
        WRITE_TO_DOCUMENT
    }

    /**
     * Turn the provided value into a document
     * based on transgressions between the default
     * value and working value.
     *
     * @param value the value that has content
     * @param defaultValue where we fetch default data from
     * @param document the mongo document we're writing to
     * @param <T> the type of the value
     * @return a written document
     */
    public static <T> Document write(T value, T defaultValue, Document document)
    {
        return translateInternal(Action.WRITE_TO_DOCUMENT, Document.class, value, defaultValue, document);
    }

    /**
     * Takes in data from a Mongo document.
     *
     * @param returnType the type that we're working with
     * @param defaultValue where we fetch our default fallback data from
     * @param document mongo doc
     * @param <T> type
     * @param <R> return type
     * @return finished document
     */
    public static <T, R> R read(Class<R> returnType, T defaultValue, Document document)
    {
        return translateInternal(Action.READ_FROM_DOCUMENT, returnType, null, defaultValue, document);
    }

    /**
     * Uses a default value and a MongoDB
     * document to create a new instance
     * of the provided type ({@code T}).
     *
     * When the action matches {@link Action#READ_FROM_DOCUMENT}
     * we'll attempt at iterating over every single field
     * in the provided class. A new instance of the same type
     * ({@code defaultValue}) will be generated - every single
     * field mutually present will be transcribed to the replicate
     * value then returned by the method.
     *
     * Whilst iterating over the fields of a class we'll
     * check for a few annotations..
     *
     * > {@link Exclude}        - skip over that field if present
     * > {@link SerializedName} - only observed when working
     *  with a {@link Document} as fields are saved to our account
     *  using the snake_case style.
     *
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
     * @param document the document from the account
     * @param <T> type of what we're working with
     * @return the new object
     */
    private static <T, R> R translateInternal(Action action, Class<R> returnType, T value, T defaultValue, Document document)
    {
        try
        {
            final long _startedAt = System.currentTimeMillis();

            System.out.println("Performing Action: " + action.name());
            System.out.println("Return Type: " + returnType.getName());

            if (value != null)
                System.out.println("Value Type: " + value.getClass().getName());

            checkState(defaultValue != null, "a default value is required for both actions");
            System.out.println("Default Value Type: " + defaultValue.getClass().getName());


            R _willReturn;

            // requires non-null: defaultValue
            if (action == Action.READ_FROM_DOCUMENT)
            {
                checkState(Stream.of(returnType.getConstructors()).anyMatch(con -> con.getParameterCount() == 0), "we require a zero args constructor for initialization");
                checkState(returnType == defaultValue.getClass(), "mismatched class types");

                final R _workingWith = returnType.newInstance();

                for (Field field : _workingWith.getClass().getDeclaredFields())
                {
                    if (ReflectionUtil.skipOver(field))
                        continue;

                    // we can't set final fields, so let's check that over
                    checkState(!Modifier.isFinal(field.getModifiers()), "can't reset a final field; be sure to exclude this field or change the modifiers.");

                    final String _name = ReflectionUtil.nameFromField(field);

                    Object _current = document.get(_name);
                    Object _default = defaultValue.getClass().getDeclaredField(field.getName()).get(defaultValue);

                    field.set(defaultValue, _current != _default ? _current : _default);
                }

                _willReturn = _workingWith;
            }
            else
            {
                // writing to document
                // iterate over the value, if it's not equal to default value, then set it
                checkState(value != null, "we require a value to work with whilst writing");

                for (Field field : value.getClass().getDeclaredFields())
                {
                    if (field.isAnnotationPresent(Exclude.class))
                        continue;

                    final String _name = ReflectionUtil.nameFromField(field);

                    Object _current = field.get(value);
                    Object _default = defaultValue.getClass().getDeclaredField(field.getName()).get(value);

                    if (_current != _default)
                    {
                        // first operation needs to be modified

                        /* document.put(_name, field.isAnnotationPresent(SaveAs.class)
                                            ? translatorFor(field.getType(), field.getAnnotation(SaveAs.class).value())
                                            : _current);
                                            */
                    }
                }

                // dirty cast - verified same type by nature of
                // how this method works
                _willReturn = (R) document;
            }

            System.out.println("Elapsed Time for Operation: " + (System.currentTimeMillis() - _startedAt) + "ms");
            // idk. all of this is just so dirty.
            return _willReturn;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return null;
    }

}
