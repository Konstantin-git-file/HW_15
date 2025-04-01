package ru.elizarov.reflection;

import ru.elizarov.reflection.annotation.Invoke;

public class A {
    @Invoke
    String m1() { return ""; }

    String m2() { return ""; }

    @Invoke
    Integer m3() { return 42; }
}