package ru.elizarov.reflection.test;

import ru.elizarov.reflection.annotation.Invoke;

class StaticMethodsTest {
    @Invoke
    public static String staticMethod() {
        return "static";
    }

    public static String notAnnotated() {
        return "ignored";
    }
}

class InstanceMethodsTest {
    @Invoke
    public String instanceMethod() {
        return "instance";
    }
}

class MethodsWithParamsTest {
    @Invoke
    public String methodWithParams(String input) {
        return input;
    }
}

class VoidMethodsTest {
    @Invoke
    public void voidMethod() {
    }
}

class PrivateMethodsTest {
    @Invoke
    private String privateMethod() {
        return "private";
    }
}

class NoDefaultConstructorTest {
    private final String value;

    public NoDefaultConstructorTest(String value) {
        this.value = value;
    }

    @Invoke
    public String testMethod() {
        return value;
    }
}