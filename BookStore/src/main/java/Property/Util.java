package com.books.BookStore.example.Property;

import com.books.BookStore.example.Config.ConfigProperty;
import com.books.BookStore.example.Config.ConfigurationManager;

public class Util {
    @ConfigProperty(propertyName = "thresholdDateBook", type = int.class)
    private static int thresholdDateBook;

    @ConfigProperty(propertyName = "markOrdersAsCompleted", type = boolean.class)
    private static boolean markOrdersAsCompleted;

    public static int getThresholdDateBook(){
        return thresholdDateBook;
    }

    public static boolean isMarkOrdersAsCompleted(){
        return markOrdersAsCompleted;
    }
    static {
        ConfigurationManager.configure(Util.class);
    }
}
