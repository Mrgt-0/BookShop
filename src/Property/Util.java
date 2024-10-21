package Property;

import BookStoreModel.BookStore;
import Config.ConfigProperty;
import Config.ConfigurationManager;

import javax.imageio.stream.FileImageInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

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
