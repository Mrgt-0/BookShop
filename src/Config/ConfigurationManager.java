package Config;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

public class ConfigurationManager {
    public static void configure(Object object) {
        configureFields(object);
    }

    private static void configureFields(Object object) {
        Class<?> clas = object.getClass();
        Field[] fields = clas.getDeclaredFields();
        Class<?> type;
        String value;
        for (Field field : fields) {
            ConfigProperty configProperty = field.getAnnotation(ConfigProperty.class);
            if (configProperty != null) {
                String configFileName = configProperty.configFileName().isEmpty() ?
                        clas.getSimpleName() + "." + field.getName() : configProperty.propertyName();
                type = configProperty.type();
                try (InputStream inputStream = ConfigurationManager.class.getClassLoader().getResourceAsStream(configFileName)) {
                    if (inputStream != null) {
                        Properties properties = new Properties();
                        properties.load(inputStream);
                        value = properties.getProperty(configProperty.propertyName());
                        if (value != null) {
                            Object convertedValue = convertToType(value, type);
                            field.setAccessible(true);
                            field.set(object, convertedValue);
                        }
                    }
                } catch (IOException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
        private static Object convertToType(String value, Class<?> type) {
            switch (type.getName()) {
                case "java.lang.String":
                    return value;
                case "int":
                case "java.lang.Integer":
                    return Integer.parseInt(value);
                case "boolean":
                case "java.lang.Boolean":
                    return Boolean.parseBoolean(value);
                case "double":
                case "java.lang.Double":
                    return Double.parseDouble(value);
                default:
                    throw new IllegalArgumentException("Unsupported type: " + type.getName());
            }
        }
}
