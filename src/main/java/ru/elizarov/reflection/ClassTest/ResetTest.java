package ru.elizarov.reflection.ClassTest;

import ru.elizarov.reflection.annotation.Default;

@Default
public class ResetTest {
    String s = "test";
    int x = 42;
    Object ob = new Object();

    @Override
    public String toString() {
        return "ResetTest{s=" + s + ", x=" + x + ", ob=" + ob + "}";
    }
}
