package ru.elizarov.reflection.test;

import org.junit.jupiter.api.Test;
import ru.elizarov.reflection.ReflectionUtils;

import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class ReflectionUtilsTest {

    @Test
    public void testCollectStaticMethods() {
        Map<String, Object> result = ReflectionUtils.collect(StaticMethodsTest.class);
        assertEquals(1, result.size());
        assertEquals("static", result.get("staticMethod"));
    }

    @Test
    public void testCollectInstanceMethods()  {
        Map<String, Object> result = ReflectionUtils.collect(InstanceMethodsTest.class);
        assertEquals(1, result.size());
        assertEquals("instance", result.get("instanceMethod"));
    }

    @Test
    public void testIgnoreMethodsWithoutAnnotation()  {
        Map<String, Object> result = ReflectionUtils.collect(StaticMethodsTest.class);
        assertFalse(result.containsKey("notAnnotated"));
    }

    @Test
    public void testIgnoreMethodsWithParameters()  {
        Map<String, Object> result = ReflectionUtils.collect(MethodsWithParamsTest.class);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testIgnoreVoidMethods() {
        Map<String, Object> result = ReflectionUtils.collect(VoidMethodsTest.class);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCollectPrivateMethods() {
        Map<String, Object> result = ReflectionUtils.collect(PrivateMethodsTest.class);
        assertEquals(1, result.size());
        assertEquals("private", result.get("privateMethod"));
    }

    @Test
    public void testNoDefaultConstructor() {
        assertThrows(RuntimeException.class, () -> ReflectionUtils.collect(NoDefaultConstructorTest.class));
    }
}