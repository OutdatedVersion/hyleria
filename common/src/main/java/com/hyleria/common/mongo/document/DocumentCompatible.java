package com.hyleria.common.mongo.document;

import org.bson.Document;

 /**
  * Something that may be saved to
  * MongoDB must implement this.
  *
  * @author Ben (OutdatedVersion)
  * @since Dec/25/2016 (1:16 AM)
  */
public interface DocumentCompatible
{

    /**
     * @return this item as a {@link Document}
     */
    Document asDocument();

    /**
     * The object this is being called on
     * will be updated. Hence it will not
     * return any new value.
     *
     * @param document the document we're reading from
     * @param <T> type of compatible item
     * @return whatever it is that we're working with
     */
    <T> T populateFromDocument(Document document);

}
