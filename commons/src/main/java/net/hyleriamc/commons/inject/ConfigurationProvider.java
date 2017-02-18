package net.hyleriamc.commons.inject;

import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.MembersInjector;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import net.hyleriamc.commons.util.Constants;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.Collections;

 /**
  * @author Ben (OutdatedVersion)
  * @since Dec/08/2016 (8:31 PM)
  */
public class ConfigurationProvider
{

    /** GSON instance */
    private final Gson GSON = new Gson();

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


    public static class ConfigurationModule extends AbstractModule
    {
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
                        {
                            if (field.getType() == )
                        }
                    }
                }
            });
        }
    }


    public static class ConfigurationInjector<T> implements MembersInjector<T>
    {
        @Override
        public void injectMembers(T instance)
        {

        }
    }

}
