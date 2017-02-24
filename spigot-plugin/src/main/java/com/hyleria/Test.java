package com.hyleria;

import com.hyleria.commons.account.Account;
import org.bson.Document;

/**
 * @author Ben (OutdatedVersion)
 * @since Feb/22/2017 (11:09 PM)
 */
public class Test
{

    public Test()
    {
        final Document _document = new Document("uuid", "03c337cd-7be0-4694-b9b0-e2fd03f57258")
                                        .append("name", "OutdatedVersion");

        final Account _account = new Account();

        System.out.println("========================");
        System.out.println("Document:");
        _account.populateFromDocument(_document);

        System.out.println("========================");
        System.out.println("JSON:");
        System.out.println(_account.asDocument().toJson());
    }

    public static void main(String[] args)
    {
        new Test();
    }

}
