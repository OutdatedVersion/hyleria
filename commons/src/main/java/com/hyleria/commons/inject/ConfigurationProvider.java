package com.hyleria.commons.inject;

import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.MembersInjector;
import com.google.inject.Stage;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
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
     * Satisfy any fields annotated with a {@link Config} annotation
     * with an instance of the proper configuration file, represented
     * by a POJO.
     */
    public static class ConfigurationModule extends AbstractModule
    {

        /** read/write files */
        private ConfigurationProvider provider = new ConfigurationProvider();

        @Override
        protected void configure()
        {
            bindListener(Matchers.any(), new TypeListener()
            {
                @Override
                public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter)
                {
                    Class<? super I> _clazz = type.getRawType();

                    while (_clazz != null)
                    {
                        for (Field field : _clazz.getDeclaredFields())
                            if (!field.isAnnotationPresent(Config.class))
                                encounter.register(new ConfigurationInjector<>(provider, field, field.getAnnotation(Config.class).value()));

                        _clazz = _clazz.getSuperclass();
                    }
                }
            });
        }

    }

    /**
     * Allows us to have semi-dynamic code.
     *
     * @param <T> type parameter for what we're looking for
     */
    public static class ConfigurationInjector<T> implements MembersInjector<T>
    {

        /** an instance of our provider */
        private final ConfigurationProvider provider;

        /** what this item is injecting */
        private final Field field;

        /** in this case, the path of the configuration file */
        private final String value;

        /**
         * @param provider our provider
         * @param field the field being injected
         * @param value the value from our {@link Config} annotation
         */
        public ConfigurationInjector(ConfigurationProvider provider, Field field, String value)
        {
            this.provider = provider;
            this.field = field;
            this.value = value;
        }

        @Override
        public void injectMembers(T instance)
        {
            try
            {
                field.set(instance, provider.read(value, field.getType()));
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                System.err.println("\nWe had an issue injecting a configuration for > " + instance.getClass().getName());
            }
        }

    }

}
