package net.hyleriamc.commons.util.config;

import com.google.gson.Gson;
import net.hyleriamc.commons.util.Constants;

import java.io.File;
import java.nio.file.Files;
import java.util.Collections;

 /**
  * @author Ben (OutdatedVersion)
  * @since Dec/08/2016 (8:31 PM)
  */
public class Configurations
{

    /** GSON instance */
    private static final Gson GSON = new Gson();

    /**
     * Read the desired configuration.
     *
     * @param dataFileName the name of the file we saved
     *                     our configuration in
     * @param clazz the type of config we're reading
     * @param <T> the type of the config
     * @return the configuration
     */
    public static <T> T read(String dataFileName, Class<T> clazz)
    {
        try
        {
            final File _file = new File(Constants.DATA_FOLDER + dataFileName);

            if (!_file.exists() || _file.isDirectory())
                throw new IllegalArgumentException("Invalid data file found at " + _file.getAbsolutePath());

            return GSON.fromJson(Files.newBufferedReader(_file.toPath()), clazz);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            System.err.println();
            System.err.println("Issue occurred whilst reading configuration file.");
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
    public static <T> T write(String dataFileName, Object object)
    {
        try
        {
            Files.write(new File(Constants.DATA_FOLDER + dataFileName).toPath(), Collections.singletonList(GSON.toJson(object)));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            System.err.println();
            System.err.println("Issue occurred whilst saving configuration file.");
        }

        return (T) object;
    }

    /**
     * Allows us to use Guice's assisted injection
     * framework to dynamically load these configurations.
     */
    public interface ConfigFactory
    {
        Configurations create(String path, Class<?> clazz);
    }

    public static class ConfigFactoryImpl implements ConfigFactory
    {

        @Override
        public Configurations create(String path, Class<?> clazz)
        {
            return null;
        }

    }

    public static class RealConfiguration implements Configuration
    {

        @Override
        public String defaultPath()
        {
            return null;
        }

    }


}
