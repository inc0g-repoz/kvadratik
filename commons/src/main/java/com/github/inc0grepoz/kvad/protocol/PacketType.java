package com.github.inc0grepoz.kvad.protocol;

import lombok.Getter;

public enum PacketType {

    // Client -> Server
    CLIENT_CHAT_MESSAGE,
    CLIENT_CONSOLE_COMMAND,
    CLIENT_DISCONNECT,
    CLIENT_KEEP_ALIVE,
    CLIENT_LOGIN,
    CLIENT_PLAYER_ANIM,
    CLIENT_PLAYER_POINT,

    // Server -> Client
    SERVER_ASSET_IMAGE,
    SERVER_ASSETS_DONE,
    SERVER_ASSETS_URL,
    SERVER_BEING_ANIM,
    SERVER_BEING_DESPAWN,
    SERVER_BEING_POINT,
    SERVER_BEING_SPAWN,
    SERVER_BEING_TELEPORT,
    SERVER_BEING_TYPE,
    SERVER_CHAT_MESSAGE,
    SERVER_CONSOLE_MESSAGE,
    SERVER_TRANSFER_CONTROL,
    SERVER_LEVEL;

    public static PacketType byId(int ordinal) {
        /*
        PacketType[] values = values();
        for (int i = 0; i < values.length; i++) {
            if (values[i].id == id) {
                return values[i];
            }
        }
        return null;
        */
        return values()[ordinal];
    }

    private final @Getter int id = ordinal();

}
