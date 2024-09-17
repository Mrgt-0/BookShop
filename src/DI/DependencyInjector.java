package DI;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class DependencyInjector {
    private static final Map<Class<?>, Object> singletonInstances = new HashMap<>();

    public static <T> T getInstance(Class<T> cls) {
        if (cls.isAnnotationPresent(Singleton.class)) {
            return (T) singletonInstances.computeIfAbsent(cls, DependencyInjector::createInstance);
        } else {
            return createInstance(cls);
        }
    }

    private static <T> T createInstance(Class<T> cls) {
        try {
            Constructor<T> constructor = cls.getDeclaredConstructor();
            constructor.setAccessible(true);
            T instance = constructor.newInstance();
            injectDependencies(instance);
            return instance;
        } catch (Exception e) {
            throw new DependencyInjectionException("Error creating instance of " + cls.getName(), e);
        }
    }

    private static void injectDependencies(Object obj) {
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                field.setAccessible(true);
                try {
                    field.set(obj, getInstance(field.getType()));
                } catch (IllegalAccessException e) {
                    throw new DependencyInjectionException("Error injecting dependency into " + obj.getClass().getName() + "." + field.getName(), e);
                }
            }
        }
    }
}

