package ru.elizarov.reflection;

import ru.elizarov.reflection.annotation.Cache;

@Cache({"getValue"})
public class CachedClass {
    public int getValue() { return (int) (Math.random() * 100); }
    public int getOther() { return (int) (Math.random() * 100); }
}
