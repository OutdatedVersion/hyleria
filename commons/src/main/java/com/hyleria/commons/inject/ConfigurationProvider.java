package com.hyleria.commons.inject;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Stage;
import com.hyleria.commons.util.Constants;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.Collections;
import java.util.function.Function;


/**
 * Read & write the proper data from
 * configuration files.
 *
 * @author Ben (OutdatedVersion)
 * @since Dec/08/2016 (8:31 PM)
 */
public class ConfigurationProvider
{

    /** GSON instance */
    private final Gson GSON = new Gson();

    /** the current state of this plugin */
    private final Stage STAGE = new File("dev_server.json").exists() ? Stage.DEVELOPMENT : Stage.PRODUCTION;

    /** Inject the proper variables & append the file extension to the provided {@link String} */
    private Function<String, String> injectAndFormat = string -> string.replaceAll("\\{env}", STAGE.name().toLowerCase()) + ".json";

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
            dataFileName = injectAndFormat.apply(dataFileName);

            final File _file = new File(Constants.DATA_FOLDER + dataFileName);

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

    /**
     * Allows us to have semi-dynamic code.
     */
    public static class ConfigurationInjector
    {

        /** an instance of our provider */
        private final ConfigurationProvider provider;

        /**
         * @param provider our provider
         */
        @Inject
        public ConfigurationInjector(ConfigurationProvider provider)
        {
            this.provider = provider;
        }

        /**
         * @param clazz the class containing the {@link Config} annotated field
         * @param <T> a type parameter for that class
         */
        public <T> void inject(Class<T> clazz)
        {
            try
            {
                for (Field field : clazz.getDeclaredFields())
                {
                    if (field.isAnnotationPresent(Config.class))
                    {
                        // chances are the field is private, so
                        // let's just do this now
                        field.setAccessible(true);

                        // provider.read(field.getAnnotation(Config.class).value(), field.getType())

                        field.set(clazz, clazz);

                        System.out.println("Field: " + field.get(this).getClass().getName());
                    }
                }
            }
            catch (IllegalAccessException ex)
            {
                throw new RuntimeException(ex);
            }
        }

    }

}
