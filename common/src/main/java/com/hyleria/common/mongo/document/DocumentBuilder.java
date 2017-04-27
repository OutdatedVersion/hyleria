package com.hyleria.common.mongo.document;

import com.google.common.collect.Lists;
import com.hyleria.common.reflect.ReflectionUtil;
import org.bson.Document;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * @author Ben (OutdatedVersion)
 * @since Feb/24/2017 (11:21 AM)
 */
public class DocumentBuilder
{

    /** the {@link Document} backing this builder */
    private Document document;

    /** an instance of what we're working w/ for reflection usage */
    private Object object;

    /** the name of the fields we should skip */
    private List<String> skipFields;

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
        this.document = checkNotNull(document, "You've already set the document!");
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
        this.object = checkNotNull(object, "You've already set the object here!");
        return this;
    }

    /**
     * Indicates that if we use one
     * of the automatic methods here
     * we should exclude the provided
     * field w/ the matching name.
     *
     * @param fieldNames a set of the names
     * @return this builder
     */
    public DocumentBuilder skipOver(String... fieldNames)
    {
        if (skipFields == null)
            skipFields = Lists.newArrayList();

        Collections.addAll(skipFields, fieldNames);

        return this;
    }

    /**
     * Insert data into the backing document
     * in our own fashion
     *
     * @param doc the document
     * @return this builder
     */
    public DocumentBuilder append(Consumer<Document> doc)
    {
        doc.accept(this.document);
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

        // respect this builder's exclusions
        if (skipFields != null && skipFields.contains(field.getName()))
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

            // convert enums
            if (field.getType().isEnum())
                _fieldValue = ((Enum) _fieldValue).name();

            // we hope BSON handles most of the type conversion
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
