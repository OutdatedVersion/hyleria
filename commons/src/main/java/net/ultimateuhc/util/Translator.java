package net.ultimateuhc.util;

import org.bson.Document;

/**
 * OutdatedVersion
 * Dec/13/2016 (4:51 PM)
 */

public class Translator
{

    private Document document;

    private Translator()
    {
        this.document = new Document();
    }

    public static Translator begin()
    {
        return new Translator();
    }

    public Translator next(String key, Object defaultValue)
    {

    }

    public Document finish()
    {

    }

}
