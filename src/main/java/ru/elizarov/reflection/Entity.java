package ru.elizarov.reflection;

import ru.elizarov.reflection.annotation.ToString;

import java.lang.reflect.*;

import static java.util.stream.IntStream.*;

public class Entity {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Class<?> clazz = this.getClass();
        sb.append(clazz.getSimpleName()).append("{");
        boolean classNo = clazz.isAnnotationPresent(ToString.class) &&
                clazz.getAnnotation(ToString.class).value() == ToString.Value.NO;

        Field[] fields = clazz.getDeclaredFields();
        range(0, fields.length).forEach(i -> {
            Field field = fields[i];
            field.setAccessible(true);

            boolean fieldNo = field.isAnnotationPresent(ToString.class) &&
                    field.getAnnotation(ToString.class).value() == ToString.Value.NO;

            boolean fieldYes = field.isAnnotationPresent(ToString.class) &&
                    field.getAnnotation(ToString.class).value() == ToString.Value.YES;

            if ((!classNo && !fieldNo) || (classNo && fieldYes)) {
                try {
                    sb.append(field.getName()).append("=").append(field.get(this));
                    if (i < fields.length - 1) sb.append(", ");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        sb.append("}");
        return sb.toString();
    }
}
