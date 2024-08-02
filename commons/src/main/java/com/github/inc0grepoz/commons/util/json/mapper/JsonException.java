package com.github.inc0grepoz.commons.util.json.mapper;

@SuppressWarnings("serial")
public class JsonException extends RuntimeException
{

    public JsonException(String message)
    {
        super(message);
    }

    public JsonException(String message, Throwable throwable)
    {
        super(message, throwable);
    }

}
