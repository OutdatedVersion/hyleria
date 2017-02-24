package com.hyleria.commons.mongo.document;

import com.hyleria.commons.reflect.ReflectionUtil;
import com.hyleria.commons.translation.Translator;
import com.hyleria.commons.translation.UseTranslator;
import org.bson.Document;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkState;

/**
 * @author Ben (OutdatedVersion)
 * @since Feb/24/2017 (11:21 AM)
 */
public class DocumentBuilder
{

    /** the {@link Document} backing this builder */
    private Document document;

    /** an instance of what we're working w/ for relfection usage */
    private Object object;

    /**
     * @return a new builder
     */
    public static DocumentBuilder create()
    {
        return new DocumentBuilder();
    }

    /**
     * @param document the document that will back this builder
     * @return this builder
     */
    public DocumentBuilder withDoc(Document document)
    {
        checkState(this.document == null, "You've already set the document!");

        this.document = document;
        return this;
    }

    /**
     * set's the backing document to
     * an empty, new one
     *
     * @return this builder
     */
    public DocumentBuilder withFreshDoc()
    {
        return withDoc(new Document());
    }

    /**
     * @param object the object to use as an accessor for
     *               reflection later on
     * @return this builder
     */
    public DocumentBuilder withObject(Object object)
    {
        checkState(this.object == null, "You've already set the object here!");

        this.object = object;
        return this;
    }

    /**
     * Insert data into this document
     *
     * @param key the key
     * @param val the value
     * @return this builder
     */
    public DocumentBuilder append(String key, Object val)
    {
        this.document.put(key, val);
        return this;
    }

    /**
     * Insert data into this document via
     * the provided field.
     *
     * @param field the field
     * @return this builder
     */
    public DocumentBuilder append(Field field)
    {
        checkState(object != null, "We require an object to use as an accessor.");

        // in some cases we don't want to actually
        // use something, so let's respect that
        if (ReflectionUtil.skipOver(field))
            return this;


        try
        {
            // allow access to private fields
            if (!field.isAccessible())
                field.setAccessible(true);

            Object _fieldValue = field.get(this.object);

            // skip undefined fields
            if (_fieldValue == null)
                return this;

            if (field.isAnnotationPresent(UseTranslator.class))
            {
                final Translator _translator = field.getAnnotation(UseTranslator.class).value().get();

                if (_fieldValue instanceof Collection)
                {
                    System.out.println(((Collection) _fieldValue).stream().map(obj -> _translator.write(obj)).collect(Collectors.toList()).toString());
                }
                else
                {
                    _fieldValue = _translator.write(_fieldValue);
                }
            }

            // convert enums
            if (field.getType().isEnum())
                _fieldValue = ((Enum) _fieldValue).name();

            document.put(ReflectionUtil.nameFromField(field), _fieldValue);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            System.err.println("Encountered issue whilst appending field.\n");
        }

        return this;
    }

    /**
     * Takes every field in the class of
     * the specified object, and adds it
     * into our document.
     *
     * @param object the object
     * @return this builder
     */
    public DocumentBuilder appendAllFields(Object object)
    {
        // set our accessor first
        withObject(object);

        // alright, let's start inserting stuff
        Arrays.stream(object.getClass().getDeclaredFields()).forEach(this::append);

        return this;
    }

    /**
     * @return the document we've been building
     */
    public Document finished()
    {
        object = null;
        return document;
    }

}
