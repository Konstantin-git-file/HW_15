package ru.elizarov;

import ru.elizarov.reflection.*;
import ru.elizarov.reflection.ClassTest.EntityTest;
import ru.elizarov.reflection.ClassTest.ResetTest;

import java.util.Map;

import static ru.elizarov.reflection.ReflectionUtils.*;


public class Main {
    public static void main(String[] args) throws Exception {
        // 8.3.1 сбор объектов
        Map<String, Object> collected = collect(A.class);
        System.out.println("Результат collect: " + collected);
        System.out.println("------------");

        // 8.3.2 дефолт
        ResetTest resetTest = new ResetTest();
        System.out.println("До reset: " + resetTest);
        reset(resetTest);
        System.out.println("После reset: " + resetTest);
        System.out.println("------------");

        // 8.3.3 стринга но через Entity
        EntityTest entityTest = new EntityTest();
        System.out.println("toString для EntityTest: " + entityTest);
        System.out.println("------------");

        // 8.3.4 валидация
        Human human = new Human(750);
        System.out.println("Объект Human: " + human);
        validate(human);
        System.out.println("------------");

        // 8.3.6 кэширование
        CachedClass cached = new CachedClass();
        cache(cached);
        System.out.println("Кэш после вызова: " + cache);
        System.out.println("getValue: " + cached.getValue()); // должно совпадать с кэшем
        System.out.println("getOther: " + cached.getOther());
    }
}