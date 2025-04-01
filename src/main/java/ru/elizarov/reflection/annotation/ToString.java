package ru.elizarov.reflection.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface ToString {
    enum Value { YES, NO }
    Value value();
}