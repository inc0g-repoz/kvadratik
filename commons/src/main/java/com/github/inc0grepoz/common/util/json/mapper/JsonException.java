package com.github.inc0grepoz.common.util.json.mapper;

@SuppressWarnings("serial")
public class JsonException extends Exception
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
