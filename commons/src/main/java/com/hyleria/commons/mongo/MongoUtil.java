package com.hyleria.commons.mongo;

import com.google.gson.annotations.SerializedName;
import com.hyleria.commons.reflect.ReflectionUtil;
import com.hyleria.commons.translation.SaveAs;
import com.simplexitymc.util.json.Exclude;
import org.bson.Document;

import java.lang.reflect.Field;

import static com.google.common.base.Preconditions.checkState;
import static com.hyleria.commons.reflect.ReflectionUtil.isPrimitive;
import static com.hyleria.commons.translation.Translators.translatorFor;

/**
  * @author Ben (OutdatedVersion)
  * @since Dec/23/2016 (5:07 PM)
  */
public class MongoUtil
{

    private static final Object ACCESSOR = new Object();

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
            System.out.println("action -> " + action);
            System.out.println("returnType -> " + returnType.getName());

//            if (action == Action.READ_FROM_DOCUMENT)
//                checkState(value != null, "'value' must not be null");
//            else
//                checkState(defaultValue != null, "'defaultValue' must not be null");


            // requires non-null: defaultValue
            if (action == Action.READ_FROM_DOCUMENT)
            {
                checkState(returnType.getClass() == defaultValue.getClass(), "mismatched class types");

                final R _workingWith = returnType.newInstance();

                for (Field field : _workingWith.getClass().getDeclaredFields())
                {
                    if (field.isAnnotationPresent(Exclude.class))
                        continue;

                    final String _name = ReflectionUtil.nameFromField(field);

                    Object _current = document.get(_name);
                    Object _default = defaultValue.getClass().getDeclaredField(field.getName()).get(ACCESSOR);

                    field.set(ACCESSOR, _current != _default ? _current : _default);
                }
            }
            else // write
            {
                // writing to document
                // iterate over the value, if it's not equal to default value, then set it

                for (Field field : value.getClass().getDeclaredFields())
                {
                    if (field.isAnnotationPresent(Exclude.class))
                        continue;

                    final String _name = ReflectionUtil.nameFromField(field);

                    Object _current = field.get(ACCESSOR);
                    Object _default = defaultValue.getClass().getDeclaredField(field.getName()).get(ACCESSOR);

                    if (_current != _default)
                    {
                        // TODO(Ben): ok
                        document.put(_name, field.isAnnotationPresent(SaveAs.class)
                                            ? translatorFor(field.getType(), field.getAnnotation(SaveAs.class).value())
                                            : _current);
                    }
                }
            }

            // an instance of 'T' that we may manipulate (final value)
            final Object _workingWith = action == Action.READ_FROM_DOCUMENT ? value.getClass().newInstance() : document;



            System.out.println("hitting class: " + _workingWith.getClass().getName());

            // iterate over what we need to set from our default
            for (Field field : _workingWith.getClass().getDeclaredFields())
            {
                System.out.println("hitting field: " + field.getName());

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
                    // grab the value we got from the account
                    Object _current = document.get(_name);

                    // imply that our reading order is opposite to the saving

                    // set the value on the new object
                    // they're guaranteed to be the same type, so no issues there
                    final Field _toUpdate = _workingWith.getClass().getField(field.getName());

                    // most of the fields are private so
                    // we need to forcefully change them
                    // butttt check just in-case..
                    if (!_toUpdate.isAccessible())
                        _toUpdate.setAccessible(true);

                    _toUpdate.set(_workingWith, _current == null
                                                ? field.get(value)
                                                : _current);
                }
                else
                {
                    // if the default value is not equal to our set value
                    // we'll commit the value into our document.

                    // default values are implied to be 'null' unless the
                    // specified field is a primitive; in which case it should
                    // be assigned to its default at initialization.
                    final Object _defaultValue = defaultValue.getClass().getField(field.getName());

                    // write different value
                    if (field.get(value) != _defaultValue)
                    {
                        // TODO(Ben): consider recursively going through each of the non-primitive fields w/ this method

                        if (isPrimitive(field))
                            document.put(_name, field.get(field));
                        else if (value.getClass().isAssignableFrom(DocumentCompatible.class))
                            document.put(_name, ((DocumentCompatible) value).asDocument());
                        else
                            throw new IllegalArgumentException("We couldn't convert [" + value.getClass().getName() + "] to a Document");
                    }
                }
            }

            // idk. all of this is just so dirty.
            return _workingWith == null ? (R) document : (R) _workingWith;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return null;
    }

}
