package com.hyleria.common.json;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/14/2017 (8:00 PM)
 */
public class GSONUtil
{

    /** a shared GSON instance to use */
    private static final Gson GSON = new Gson();

    /**
     * Reads the specified file (assumes it
     * is infact JSON data), and runs it
     * through a GSON instance to convert
     * it into a Java object.
     *
     * @param file the name of the file
     * @param clazz the class
     * @param gson a gson instance
     * @param <T> type of the class
     * @return an object
     */
    public static <T> T fromFile(String file, Class<T> clazz, Gson gson)
    {
        try (FileReader reader = new FileReader(new File(file)))
        {
            return gson.fromJson(reader, clazz);
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * View {@link #fromFile(String, Class, Gson)}
     * Uses the GSON instance bound to this class
     *
     * @param file the file name
     * @param clazz the class
     * @param <T> the type of the class
     * @return the object
     */
    public static <T> T fromFile(String file, Class<T> clazz)
    {
        return fromFile(file, clazz, GSON);
    }

}
