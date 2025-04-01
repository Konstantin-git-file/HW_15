package ru.elizarov.reflection;

import ru.elizarov.reflection.annotation.Validate;
import ru.elizarov.reflection.ClassTest.HumanTest;

@Validate({HumanTest.class})
public class Human {
    public int age;

    public Human(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Human{age=" + age + "}";
    }
}
