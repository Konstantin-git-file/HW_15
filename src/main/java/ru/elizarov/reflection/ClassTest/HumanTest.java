package ru.elizarov.reflection.ClassTest;

import ru.elizarov.reflection.Human;

public class HumanTest {
    public static String test(Object obj) {
        Human h = (Human) obj;
        if (h.age < 1 || h.age > 200) {
            return "возраст не в диапазоне от 1 до 200";
        }
        return "OK";
    }
}