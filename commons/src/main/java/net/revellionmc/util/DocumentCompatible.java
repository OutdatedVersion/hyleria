package net.revellionmc.util;

import org.bson.Document;

/**
 * OutdatedVersion
 * Dec/25/2016 (1:16 AM)
 */

public interface DocumentCompatible
{

    /**
     * @return whatever this interface has to do
     *          with as a MongoDB document
     */
    Document asDocument();

}
