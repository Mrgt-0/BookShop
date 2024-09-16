package Property;

import BookStoreModel.BookStore;

import javax.imageio.stream.FileImageInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Util {
    private static final String applicationFile="C:\\javaProjects\\BookShop\\src\\resource\\application.properties";
    private static int thresholdDateBook;
    private static boolean markOrdersAsCompleted;

    static {
        Properties properties=new Properties();
        try{
            properties.load(new FileInputStream(applicationFile));
            thresholdDateBook=Integer.parseInt(properties.getProperty("thresholdDateBook", "6"));
            markOrdersAsCompleted=Boolean.parseBoolean(properties.getProperty("markOrdersAsCompleted", "true"));
        } catch (IOException e) {
            System.out.println("Error: "+e.getMessage());
            e.printStackTrace();
            thresholdDateBook=6;
            markOrdersAsCompleted=true;
            throw new ExceptionInInitializerError(e);
        }
    }

    public static int getThresholdDateBook(){
        return thresholdDateBook;
    }

    public static boolean isMarkOrdersAsCompleted(){
        return markOrdersAsCompleted;
    }
}
