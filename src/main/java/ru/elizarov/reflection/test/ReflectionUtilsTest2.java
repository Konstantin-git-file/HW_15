package ru.elizarov.reflection.test;

import org.junit.jupiter.api.Test;
import ru.elizarov.reflection.ReflectionUtils;
import ru.elizarov.reflection.ValidateException;
import ru.elizarov.reflection.annotation.Cache;
import ru.elizarov.reflection.annotation.Default;
import ru.elizarov.reflection.annotation.Invoke;
import ru.elizarov.reflection.annotation.Validate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ReflectionUtilsTest2  {

    @Test
    public void testCollectWithInvokeAnnotatedMethods() {
        @SuppressWarnings("unused")
        static class TestClass {
            @Invoke
            int getValue() {
                return 42;
            }

            @Invoke
            String getName() {
                return "Test";
            }
        }

        Map<String, Object> result = ReflectionUtils.collect(TestClass.class);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(42, result.get("getValue"));
        assertEquals("Test", result.get("getName"));
    }

    @Test
    public void testCollectWithStaticMethods() {
        class TestClass {
            @Invoke
            static int getStaticValue() {
                return 100;
            }
        }

        Map<String, Object> result = ReflectionUtils.collect(TestClass.class);
        assertNotNull(result, "Результат не должен быть null");
        assertEquals(1, result.size(), "Неверное количество методов");
        assertEquals(100, result.get("getStaticValue"), "Неверное значение для стат метода");
    }

    @Test
    public void testResetFieldsToDefaultValues() {
        class TestClass {
            @Default
            int value = 10;

            @Default
            String name = "OldName";
        }

        TestClass obj = new TestClass();
        ReflectionUtils.reset(obj);

        assertEquals(0, obj.value, "Поле value должно быть сброшено к дефолту");
        assertNull(obj.name, "Поле name должно быть сброшено к дефолту (null)");
    }

    @Test
    public void testResetWithoutDefaultAnnotation() {
        class TestClass {
            int value = 10;
            String name = "OldName";
        }

        TestClass obj = new TestClass();
        ReflectionUtils.reset(obj);

        assertEquals(10, obj.value, "Поле value не должно быть сброшено, так как нет аннотации @Default");
        assertEquals("OldName", obj.name, "Поле name не должно быть сброшено, так как нет аннотации @Default");
    }

    @Test
    public void testValidateSuccess() {
        class TestValidator {
            public static String test(Object obj) {
                return "OK"; // валидный случай
            }
        }

        @Validate(TestValidator.class)
        class TestClass {}

        TestClass obj = new TestClass();
        assertDoesNotThrow(() -> ReflectionUtils.validate(obj), "Валидация должна быть ОК");
    }

    @Test
    public void testValidateFailure() {
        class TestValidator {
            public static String test(Object obj) {
                return "Валидация не пройдена";
            }
        }

        @Validate(TestValidator.class)
        class TestClass {}

        TestClass obj = new TestClass();
        ValidateException exception = assertThrows(ValidateException.class, () -> ReflectionUtils.validate(obj),
                "Должно быть выброшено исключение ValidateException");
        assertEquals("Валидация не пройдена", exception.getMessage(), "Сообщение не соответствует");
    }

    @Test
    public void testCacheMethodResults() {
        class TestClass {
            @Cache
            String getValue() {
                return "CachedValue";
            }
        }

        TestClass obj = new TestClass();
        ReflectionUtils.cache(obj);

        assertTrue(ReflectionUtils.cache.containsKey("TestClass.getValue"), "Ключ должен быть добавлен в кэш");
        assertEquals("CachedValue", ReflectionUtils.cache.get("TestClass.getValue"), "Значение не соответствует");
    }

    @Test
    public void testCacheWithSpecificMethods() {
        class TestClass {
            String getValue1() {
                return "Value1";
            }

            String getValue2() {
                return "Value2";
            }
        }

        @Cache("getValue1")
        class CachedTestClass extends TestClass {}

        CachedTestClass obj = new CachedTestClass();
        ReflectionUtils.cache(obj);

        assertTrue(ReflectionUtils.cache.containsKey("CachedTestClass.getValue1"), "Метод getValue1 должен быть закеширован");
        assertFalse(ReflectionUtils.cache.containsKey("CachedTestClass.getValue2"), "Метод getValue2 не должен быть закеширован");
    }
}
