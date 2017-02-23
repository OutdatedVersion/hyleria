package com.hyleria;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;
import com.hyleria.commons.mongo.DefaultValue;
import com.hyleria.commons.mongo.MongoUtil;
import org.bson.Document;

/**
 * @author Ben (OutdatedVersion)
 * @since Feb/22/2017 (11:09 PM)
 */
public class Test
{

    public Test()
    {
        final Document _write = MongoUtil.write(new DataClass("Ben", 1, "Ben OutdatedVersion"), DataClass.DEFAULT_VAL, new Document());

        System.out.println("Write:");
        System.out.println(_write.toJson());
        System.out.println();
        System.out.println();

        // -----------------------

        final DataClass _data = MongoUtil.read(DataClass.class, DataClass.DEFAULT_VAL, _write);

        System.out.println("Read:");
        System.out.println(_data.toString());
        System.out.println();
        System.out.println();
    }

    public static void main(String[] args)
    {
        new Test();
    }

    public static class DataClass
    {
        @DefaultValue
        public static final DataClass DEFAULT_VAL = new DataClass("Default", 5, "Default");

        public String name;
        public int level = 1;

        @SerializedName ( "full_name" )
        public String fullName;

        public DataClass(String name, int level, String fullName)
        {
            this.name = name;
            this.level = level;
            this.fullName = fullName;
        }

        @Override
        public String toString()
        {
            return Objects.toStringHelper(this).add("name", name).add("level", level).add("fullName", fullName).toString();
        }
    }

}
