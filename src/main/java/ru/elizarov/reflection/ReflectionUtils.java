package ru.elizarov.reflection;

import lombok.SneakyThrows;
import ru.elizarov.reflection.annotation.Cache;
import ru.elizarov.reflection.annotation.Default;
import ru.elizarov.reflection.annotation.Invoke;
import ru.elizarov.reflection.annotation.Validate;

import java.lang.reflect.*;
import java.util.*;

public class ReflectionUtils {
    private static final Map<Class<?>, Object> DEFAULT_VALUES = new HashMap<>();

    static {
        DEFAULT_VALUES.put(String.class, null);
        DEFAULT_VALUES.put(int.class, 0);
        DEFAULT_VALUES.put(Integer.class, 0);
        DEFAULT_VALUES.put(Object.class, null);
    }

    @SneakyThrows
    public static Map<String, Object> collect(Class<?>... classes) {
        Map<String, Object> result = new HashMap<>();
        for (Class<?> clazz : classes) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Invoke.class) &&
                        method.getParameterCount() == 0 &&
                        !method.getReturnType().equals(void.class)) {

                    method.setAccessible(true);
                    Object value;
                    if (Modifier.isStatic(method.getModifiers())) {
                        value = method.invoke(null);
                    } else {
                        try {
//                        Object instance = clazz.getDeclaredConstructor().newInstance();
//                        value = method.invoke(instance);
                            Constructor<?> constructor = clazz.getDeclaredConstructor();
                            constructor.setAccessible(true);
                            value = method.invoke(constructor.newInstance());
                        } catch (NoSuchMethodException e) {
                            throw new RuntimeException("класс без конструктора: " + clazz.getName(), e);
                        }
                    }
                    result.put(method.getName(), value);
                }
            }
        }
        return result;
    }

    @SneakyThrows
    public static void reset(Object... objects) {
        for (Object obj : objects) {
            Class<?> clazz = obj.getClass();
            boolean classDefault = clazz.isAnnotationPresent(Default.class);
            resetFields(obj, clazz, classDefault);
        }
    }

    @SneakyThrows
    private static void resetFields(Object obj, Class<?> clazz, boolean classDefault) {
        if (clazz == null) return;
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (classDefault || field.isAnnotationPresent(Default.class)) {
                field.setAccessible(true);
                Object defaultValue = DEFAULT_VALUES.getOrDefault(field.getType(), null);
                field.set(obj, defaultValue);
            }
        }
        resetFields(obj, clazz.getSuperclass(), classDefault);
    }

    @SneakyThrows
    public static void validate(Object... objects) {
        for (Object obj : objects) {
            Class<?> clazz = obj.getClass();
            if (clazz.isAnnotationPresent(Validate.class)) {
                Validate validate = clazz.getAnnotation(Validate.class);
                for (Class<?> testClass : validate.value()) {
                    Method testMethod = testClass.getMethod("test", Object.class);
                    String result = (String) testMethod.invoke(null, obj);
                    if (!"OK".equals(result)) {
                        throw new ValidateException(result);
                    }
                }
            }
        }
    }

    public static final Map<String, Object> cache = new HashMap<>();

    @SneakyThrows
    public static void cache(Object... objects) {
        for (Object obj : objects) {
            Class<?> clazz = obj.getClass();
            if (clazz.isAnnotationPresent(Cache.class)) {
                Cache cacheAnn = clazz.getAnnotation(Cache.class);
                String[] methodsToCache = cacheAnn.value();
                boolean cacheAll = methodsToCache.length == 0;

                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    if (cacheAll || Arrays.asList(methodsToCache).contains(method.getName())) {
                        method.setAccessible(true);
                        String key = clazz.getName() + "." + method.getName();
                        if (!cache.containsKey(key)) {
                            Object result = method.invoke(obj);
                            cache.put(key, result);
                        }
                    }
                }
            }
        }
    }
}
