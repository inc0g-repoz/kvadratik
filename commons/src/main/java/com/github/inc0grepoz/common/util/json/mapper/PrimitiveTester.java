package com.github.inc0grepoz.common.util.json.mapper;

public class PrimitiveTester
{

    PrimitiveTester() {}

    public boolean isPrimitiveType(Object source)
    {
        return isPrimitiveClass(source.getClass());
    }

    public boolean isPrimitiveClass(Class<?> clazz)
    {
        return clazz == Integer.class   || clazz == int.class     ||
               clazz == Byte.class      || clazz == byte.class    ||
               clazz == Character.class || clazz == char.class    ||
               clazz == Boolean.class   || clazz == boolean.class ||
               clazz == Double.class    || clazz == double.class  ||
               clazz == Float.class     || clazz == float.class   ||
               clazz == Long.class      || clazz == long.class    ||
               clazz == Short.class     || clazz == short.class   ||
               clazz == Void.class      || clazz == void.class;
    }

    public boolean isDefaultValue(Object object)
    {
        return object == null ||
               object instanceof Boolean && !((boolean) object) ||
               object instanceof Number  && ((Number) object).doubleValue() == 0;
    }

}
