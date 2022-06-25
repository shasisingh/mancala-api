package com.shashi.bol.mancala.web.game.v1;

import java.lang.reflect.Field;

public final class ReflectionUtil {
    public static void set(Object object, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }
}
