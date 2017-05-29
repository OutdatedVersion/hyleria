package com.hyleria.common.config;

import com.google.gson.Gson;
import com.google.inject.Singleton;
import com.hyleria.common.reference.Constants;

import java.io.File;
import java.nio.file.Files;
import java.util.Collections;
import java.util.function.Function;


/**
 * Read and write the proper data from
 * configuration files.
 *
 * @author Ben (OutdatedVersion)
 * @since Dec/08/2016 (8:31 PM)
 */
@Singleton
public class ConfigurationProvider
{

    /** GSON instance */
    private final Gson GSON = new Gson();

    /** Inject the proper variables & append the file extension to the provided {@link String} */
    private Function<String, String> injectAndFormat = string -> string.replaceAll("\\{env}", Constants.ENV.name().toLowerCase()) + ".json";

    /**
     * Read the desired configuration.
     *
     * @param dataFileName the name of the file we saved
     *                     our configuration in
     * @param clazz the type of config we're reading
     * @param <T> the type of the config
     * @return the configuration
     */
    public <T> T read(String dataFileName, Class<T> clazz)
    {
        try
        {
            final File _file = new File(Constants.DATA_FOLDER + injectAndFormat.apply(dataFileName));

            if (!_file.exists() || _file.isDirectory())
                throw new IllegalArgumentException("Invalid data file found at " + _file.getAbsolutePath());

            return GSON.fromJson(Files.newBufferedReader(_file.toPath()), clazz);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            System.err.println("\nIssue occurred whilst reading configuration file.");
        }

        return null;
    }

    /**
     * Saves the provided {@code Object} to a file
     * in JSON form.
     *
     * @param dataFileName the name of the file we'd
     *                     like to save our object to
     * @param object the object that we're saving
     * @param <T> the type of the object. pretty messy.
     * @return our object we (hopefully) just saved
     */
    public <T> T write(String dataFileName, Object object)
    {
        try
        {
            Files.write(new File(Constants.DATA_FOLDER + dataFileName).toPath(),
                                 Collections.singletonList(GSON.toJson(object)));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            System.err.println("\nIssue occurred whilst saving configuration file.");
        }

        return (T) object;
    }

}
