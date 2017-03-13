package com.hyleria.common.mongo.document;

import org.bson.Document;

/**
 * Indicates that the interfaced item
 * will translate into a document, but
 * not from one. Chances are this is
 * because it's just unnecessary & someone
 * is lazy.
 *
 * @author Ben (OutdatedVersion)
 * @since Feb/24/2017 (12:29 PM)
 */
public interface DocumentConvertable extends DocumentCompatible
{

    /**
     * @inheritDoc
     */
    @Override
    Document asDocument();

    @Override
    default <T> T populateFromDocument(Document document)
    {
        // not using here
        return null;
    }

}
