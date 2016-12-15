package net.ultimateuhc.util;

import org.bson.Document;

/**
 * OutdatedVersion
 * Dec/13/2016 (4:51 PM)
 */

public class MongoUtil
{

    public static <T> T translate(Document document, Object object, Translator translator)
    {
        return (T) object;
    }

}
