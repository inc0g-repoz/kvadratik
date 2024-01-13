package com.github.inc0grepoz.common.util.json.mapper;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;
import java.util.UUID;

/**
 * Serializes Java instances into JSON string values.
 * 
 * @author inc0g-repoz
 */
public class JsonSerializer
{

    private final PrimitiveTester typeTester = new PrimitiveTester();

    String serialize(Object instance)
    throws Throwable
    {
        if (instance == null)
        {
            return null;
        }
        else if (typeTester.isPrimitiveType(instance))
        {
            return typeTester.isDefaultValue(instance) ? null : String.valueOf(instance);
        }
        else if (instance.getClass() == String.class)
        {
            return "\"" + ((String) instance).replace("\"", "\\\"") + "\"";
        }
        else if (instance instanceof Enum) {
            return "\"" + ((Enum<?>) instance).name() + "\"";
        }
        else if (instance.getClass() == UUID.class)
        {
            return "\"" + instance.toString() + "\"";
        }
        else if (instance.getClass().isArray())
        {
            // Creating a string joiner to write all values
            // of the array and delimit them by a comma
            StringJoiner arrayJoiner = new StringJoiner(", ");

            // Iterating through all the array elements
            for (int i = 0; i < Array.getLength(instance); i++)
            {
                arrayJoiner.add(serialize(Array.get(instance, i)));
            }

            // Joining the array collection into the compound
            return "[" + arrayJoiner + "]";
        }
        else if (instance instanceof Collection)
        {
            // Creating a string joiner to write all values
            // of the collection and delimit them by a comma
            StringJoiner collectionJoiner = new StringJoiner(", ");

            // Iterating through all the collection elements
            for (Object element: (Iterable<?>) instance)
            {
                collectionJoiner.add(serialize(element));
            }

            // Joining the serialized collection into the compound
            return "[" + collectionJoiner + "]";
        }
        else if (instance instanceof Map)
        {
            // Creating a string joiner to write all values
            // of the map and delimit them by a comma
            StringJoiner mapJoiner = new StringJoiner(", ");

            // Iterating through all the key value pairs
            Map<?, ?> map = (Map<?, ?>) instance;
            for (Entry<?, ?> entry: map.entrySet())
            {
                mapJoiner.add(serialize(entry.getKey()) + ": " + serialize(entry.getValue()));
            }

            // Joining the serialized map into the compound
            return "{" + mapJoiner + "}";
        }

        // Creating a string joiner to write each field
        // and it's value in a string representation
        StringJoiner compoundJoiner = new StringJoiner(", ");

        // Creating a class reference to walk through all fields
        // declared in the passed object including ones inherited
        // from it's superclasses
        Class<?> clazz = instance.getClass();

        // Temporary object in field pointer
        Object temp;

        // Serializing fields values for each inherited class
        // Breaking the loop if no more inherited classes found
        while (clazz != null)
        {

            // Walking through all fields of the object class
            // or it's super class if present
            for (Field field: clazz.getDeclaredFields())
            {
                if (Modifier.isFinal(field.getModifiers())  ||
                    Modifier.isStatic(field.getModifiers()) ||
                    Modifier.isTransient(field.getModifiers()))
                {
                    continue; // Skipping if it's transient
                }

                // Retrieving the field value
                field.setAccessible(true);
                temp = field.get(instance);

                // Only write non-null values
                if (!typeTester.isDefaultValue(temp))
                {
                    compoundJoiner.add("\"" + field.getName() + "\": " + serialize(temp));
                }
            }

            clazz = clazz.getSuperclass();
        }

        return "{" + compoundJoiner + "}";
    }

}
