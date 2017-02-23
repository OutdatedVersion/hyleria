package com.hyleria.commons.mongo;

import org.bson.Document;

 /**
  * @author Ben (OutdatedVersion)
  * @since Dec/25/2016 (1:16 AM)
  */
public interface DocumentCompatible
{

    /**
     * @return whatever this interface has to do
     *          with as a MongoDB document
     */
    Document asDocument();

}
