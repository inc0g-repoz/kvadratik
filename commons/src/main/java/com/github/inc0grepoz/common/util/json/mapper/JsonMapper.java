package com.github.inc0grepoz.common.util.json.mapper;

import lombok.Getter;

/**
 * Serializes and deserializes Java instances.
 * 
 * @author inc0g-repoz
 */
@Getter
public class JsonMapper {

    private final JsonSerializer serializer;
    private final JsonDeserializer deserializer;

    {
        serializer = new JsonSerializer();
        deserializer = new JsonDeserializer();
    }

    /**
     * Serializes an instance in the argument and returns
     * it's string representation.
     * 
     * @param instance an instance that needs to be serialized
     * @return an object string representation
     * @throws JsonException if fails to serialize an instance
     */
    public String serialize(Object instance)
    throws JsonException {
        try {
            return serializer.serialize(instance);
        } catch (Throwable t) {
            throw new JsonException("Failed to serialize an instance of " + instance.getClass().getName(), t);
        }
    }

    /**
     * Returns a deserialized instance of the class in the argument
     * from it's JSON string representation. Deserializes instances
     * of generic classes if type arguments are provided.
     * 
     * @param <T> a type of instance that needs to be deserialized
     * @param json a string representation of an instance
     * @param clazz a type of instance that needs to be deserialized
     * @param typeArguments optional type arguments
     * @return a deserialized instance of class
     * @throws JsonException if fails to deserialize an instance
     */
    public <T> T deserialize(String json, Class<T> clazz, Class<?>... typeArguments)
    throws JsonException {
        try {
            return deserializer.deserialize(json, clazz, typeArguments);
        } catch (Throwable t) {
            throw new JsonException("Failed to deserialize an instance of " + clazz.getName(), t);
        }
    }

}
