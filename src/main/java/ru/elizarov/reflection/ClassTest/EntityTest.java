package ru.elizarov.reflection.ClassTest;

import ru.elizarov.reflection.Entity;
import ru.elizarov.reflection.annotation.ToString;

public class EntityTest extends Entity {
    String s = "hello";
    @ToString(ToString.Value.YES)
    int x = 42;
}
