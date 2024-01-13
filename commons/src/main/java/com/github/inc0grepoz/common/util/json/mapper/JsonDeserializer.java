package com.github.inc0grepoz.common.util.json.mapper;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Deserializes Java instances from JSON string values.
 * 
 * @author inc0g-repoz
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class JsonDeserializer
{

    <T> T deserialize(String json, Class<T> clazz, Class<?>... typeArguments)
    throws Throwable
    {
        // Returning strings and primitives instantly
        if (clazz == String.class)
        {
            return (T) json;
        }
        if (clazz == Integer.class || clazz == int.class)
        {
            return (T) Integer.valueOf(json);
        }
        if (clazz == Byte.class || clazz == byte.class)
        {
            return (T) Byte.valueOf(json);
        }
        if (clazz == Character.class || clazz == char.class)
        {
            return (T) Character.valueOf(json.charAt(0));
        }
        if (clazz == Boolean.class || clazz == boolean.class)
        {
            return (T) Boolean.valueOf(json);
        }
        if (clazz == Double.class || clazz == double.class)
        {
            return (T) Double.valueOf(json);
        }
        if (clazz == Float.class || clazz == float.class)
        {
            return (T) Float.valueOf(json);
        }
        if (clazz == Long.class || clazz == long.class)
        {
            return (T) Long.valueOf(json);
        }
        if (clazz == Short.class || clazz == short.class)
        {
            return (T) Short.valueOf(json);
        }
        if (clazz == UUID.class)
        {
            return (T) UUID.fromString(json);
        }
        if (Enum.class.isAssignableFrom(clazz))
        {
            return (T) clazz.getField(json).get(null);
        }

        // Stores the currently parsed expression
        StringBuilder stringBuilder = new StringBuilder();

        boolean quote = false; // JSON primitives quotes

        if (clazz.isArray())
        {
            // Not specifying the generic parameter
            // to avoid excessive complexity
            ArrayList list = new ArrayList();

            // Reading and adding elements
            addJsonArrayElements(json, list, clazz.getComponentType());

            // Converting the collection into an array
            Object array = Array.newInstance(clazz.getComponentType(), list.size());
            for (int i = 0; i < list.size(); i++)
            {
                Array.set(array, i, list.get(i));
            }

            return (T) array;
        }

        // Checking if the object can be assigned to a collection field
        if (Collection.class.isAssignableFrom(clazz))
        {
            // Not specifying the generic parameter
            // to avoid excessive complexity
            Collection collection;

            // Using different implementations depending on interfaces
            if (clazz == Queue.class || clazz == LinkedList.class)
            {
                collection = new LinkedList();
            }
            else if (clazz == Set.class || clazz == HashSet.class)
            {
                collection = new HashSet();
            }
            else
            {
                collection = new ArrayList();
            }

            // Reading and adding elements
            addJsonArrayElements(json, collection, typeArguments[0]);

            return (T) collection; // Returning the collection
        }

        // Checking if the object can be assigned to a map field
        boolean map = Map.class.isAssignableFrom(clazz);

        // Creating a pointer to an instance of the class in the argument
        // to recursively write the values from the string
        T instance = map && clazz.isInterface() ? (T) new HashMap()
                : clazz.getConstructor().newInstance();

        // Stores the name of the field in which the object is written
        String fieldName = null;

        // Toggles one out of listed above modes of the parser
        JsonDeserializerState state = JsonDeserializerState.EXPECT_OBJECT_OPEN;

        char[] chars = json.toCharArray(); // Preparing the compound to be read
        int brackets = 0; // Inner JSON compound or array brackets

        // Reading the JSON string in the argument char by char
        for (int i = 0; i < chars.length; i++)
        {
            switch (state)
            {
                case EXPECT_OBJECT_OPEN: // Expecting a '{'
                    if (chars[i] == '{')
                    {
                        // Expecting a quote in the beginning of a key
                        // in further iterations
                        state = JsonDeserializerState.EXPECT_KEY_QUOTE_BEGIN;
                    }
                    break;

                case EXPECT_KEY_QUOTE_BEGIN:
                    if (chars[i] == '"')
                    {
                        // Expecting characters from the key name
                        // in further iterations
                        state = JsonDeserializerState.EXPECT_KEY_QUOTE_END;
                    }
                    break;

                case EXPECT_KEY_QUOTE_END:
                    if (chars[i] == '"' && chars[i - 1] != '\\')
                    {
                        fieldName = stringBuilder.toString();
                        stringBuilder.setLength(0); // Clearing the string builder

                        state = JsonDeserializerState.EXPECT_KEY_VALUE_DELIMITER;
                    }
                    else
                    {
                        // Writing a character from the key name
                        stringBuilder.append(chars[i]);
                    }
                    break;

                case EXPECT_KEY_VALUE_DELIMITER:
                    if (chars[i] == ':')
                    {
                        state = JsonDeserializerState.EXPECT_VALUE;
                    }
                    break;

                case EXPECT_VALUE:
                    if (Character.isWhitespace(chars[i]))
                    {
                        break; // Skipping a character
                    }

                    if (chars[i] == '"')
                    {
                        state = JsonDeserializerState.EXPECT_VALUE_STRING_END;
                        break; // Skipping a character
                    }

                    // Writing a character from the value
                    stringBuilder.append(chars[i]);

                    if (chars[i] == '{')
                    {
                        brackets = 1; // Opening one JSON compound clause
                        state = JsonDeserializerState.EXPECT_VALUE_JSON_END;
                    }
                    else if (chars[i] == '[')
                    {
                        brackets = 1; // Opening one JSON compound clause
                        // Expecting an inner compound ']' in further iterations
                        state = JsonDeserializerState.EXPECT_VALUE_ARRAY_END;
                    }
                    else
                    {
                        // Expecting a '}' or an entries delimiter
                        state = JsonDeserializerState.EXPECT_OBJECT_END_OR_ENTRIES_DELIMITER;
                    }
                    break;

                case EXPECT_VALUE_STRING_END:
                    if (chars[i] == '"' && chars[i - 1] != '\\')
                    {
                        state = JsonDeserializerState.EXPECT_OBJECT_END_OR_ENTRIES_DELIMITER;
                    }
                    else
                    {
                        // Writing a character from the value
                        stringBuilder.append(chars[i]);
                    }
                    break;

                case EXPECT_VALUE_ARRAY_END:

                    // Writing a character from the value
                    stringBuilder.append(chars[i]);

                    if (chars[i] == '[' && !quote)
                    {
                        brackets++; // Opening one array bracket
                    }
                    else if (chars[i] == ']' && !quote)
                    {
                        brackets--; // Closing one array bracket

                        if (brackets == 0)
                        {
                            state = JsonDeserializerState.EXPECT_OBJECT_END_OR_ENTRIES_DELIMITER;
                        }
                    }
                    else if (chars[i] == '"' && chars[i - 1] != '\\')
                    {
                        quote = !quote;
                    }
                    break;

                case EXPECT_VALUE_JSON_END:

                    // Writing a character from the value
                    stringBuilder.append(chars[i]);

                    if (chars[i] == '{' && !quote)
                    {
                        brackets++; // Opening one JSON compound clause
                    }
                    else if (chars[i] == '}' && !quote)
                    {
                        brackets--; // Closing one JSON compound clause

                        if (brackets == 0)
                        {
                            state = JsonDeserializerState.EXPECT_OBJECT_END_OR_ENTRIES_DELIMITER;
                        }
                    }
                    else if (chars[i] == '"' && chars[i - 1] != '\\')
                    {
                        quote = !quote;
                    }
                    break;

                case EXPECT_OBJECT_END_OR_ENTRIES_DELIMITER:
                    if (Character.isWhitespace(chars[i]))
                    {
                        break; // Skipping a character
                    }

                    if (chars[i] == '"')
                    {
                        // Expecting a string ending quote in further iterations
                        state = JsonDeserializerState.EXPECT_VALUE_STRING_END;
                        break; // Skipping a character
                    }

                    if (chars[i] == ',')
                    {
                        putValue(instance, map, fieldName, stringBuilder.toString(), clazz, typeArguments);
                        stringBuilder.setLength(0); // Clearing the string builder
                        state = JsonDeserializerState.EXPECT_KEY_QUOTE_BEGIN;
                    }
                    else if (chars[i] == '}')
                    {
                        putValue(instance, map, fieldName, stringBuilder.toString(), clazz, typeArguments);
                        stringBuilder.setLength(0); // Clearing the string builder
                    }
                    else
                    {
                        // Writing a character from the value
                        stringBuilder.append(chars[i]);
                    }

                default:
            }
        }

        return instance;
    }

    private <T> void addJsonArrayElements(String json, Collection rawCollection, Class<T> elementType)
    throws Throwable
    {
        // Stores the currently parsed expression
        StringBuilder stringBuilder = new StringBuilder();

        char[] chars = json.toCharArray(); // Preparing the compound to be read
        boolean quote = false; // Array strings quotes
        int brackets = 0; // Brackets count

        // Reading through all tokens
        for (int i = 0; i < chars.length; i++)
        {
            // Found a quote
            if (chars[i] == '"' && brackets < 2)
            {
                // Toggling the quotes mode and skipping it
                quote = !quote;
                i++; // Skipping the next character
            }

            // Quotes enabled
            if (quote)
            {
                // Handling escaped quotes
                if (chars[i] == '\\' && chars[i + 1] == '"')
                {
                    stringBuilder.append("\"");
                    i++; // Skipping the next character
                }
                else
                {
                    stringBuilder.append(chars[i]);
                }
                continue;
            }

            // Quotes disabled
            switch (chars[i])
            {
                case '[':
                    if (++brackets > 1)
                    {
                        // Writing a character from a value
                        stringBuilder.append(chars[i]);
                    }
                    break;

                case ']':
                    if (--brackets > 0)
                    {
                        // Writing a character from a value
                        stringBuilder.append(chars[i]);
                    }
                    else
                    {
                        // Adding the last element into the collection
                        rawCollection.add(deserialize(stringBuilder.toString(), elementType));
                        stringBuilder.setLength(0); // Clearing the string buffer
                    }
                    break;

                case '{':
                    // Writing a character from a value
                    stringBuilder.append(chars[i]);
                    brackets++;
                    break;

                case '}':
                    // Writing a character from a value
                    stringBuilder.append(chars[i]);
                    brackets--;
                    break;

                case ',':
                    if (brackets > 1)
                    {
                        // Writing a character from a value
                        stringBuilder.append(chars[i]);
                    }
                    else
                    {
                        // Adding a new element into the collection
                        rawCollection.add(deserialize(stringBuilder.toString(), elementType));
                        stringBuilder.setLength(0); // Clearing the string buffer
                    }
                    break;

                default:
                    // Not writing whitespaces in the beginning
                    if (stringBuilder.length() == 0 && Character.isWhitespace(chars[i]))
                    {
                        break; // Skipping a character
                    }

                    // Writing a character from a value
                    stringBuilder.append(chars[i]);
            }
        }
    }

    private <T> void putValue(T instance, boolean map, String fieldName, String json,
            Class<?> clazz, Class<?>... typeArguments)
    throws Throwable
    {
        if (map)
        {
            // Deserializing and putting the object into the map
            ((Map) instance).put(fieldName, deserialize(json, typeArguments[1]));
        }
        else
        {
            // Deserializing and writing the object into the field
            Field field = findField(clazz, fieldName);

            if (field == null)
            {
                return; // Not writing if the field is missing
            }

            field.setAccessible(true);
            field.set(instance, deserialize(json, field.getType(),
                    getFieldTypeParameters(field)));
        }
    }

    private Field findField(Class<?> clazz, String fieldName)
    throws NoSuchFieldException, SecurityException
    {
        Field field; // The field pointer

        // Looking for fields in the object class and it's inherited class
        // Breaking the loop if no more inherited classes found
        while (clazz != null)
        {
            try
            {
                field = clazz.getDeclaredField(fieldName);

                if (field != null)
                {
                    // Returning the field found in the object class
                    // or one of it's superclasses
                    return field;
                }
            }
            catch (NoSuchFieldException e)
            {}

            // Looking for the field in the superclass
            // in the next iteration
            clazz = clazz.getSuperclass();
        }

        return null;
    }

    private Class<?>[] getFieldTypeParameters(Field field)
    {
        Type type = field.getGenericType(); // Retrieving the field type

        if (type instanceof ParameterizedType) // Returning type arguments for parameterized types
        {
            // Streaming all type parameters and map cast them into classes
            return Stream.of(((ParameterizedType) field.getGenericType()).getActualTypeArguments())
                    .map(Class.class::cast).toArray(Class<?>[]::new);
        }
        else // Otherwise, an empty array
        {
            return new Class<?>[] {};
        }
    }

}
