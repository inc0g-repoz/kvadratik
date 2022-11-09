package com.github.inc0grepoz.kvad.protocol;

public enum PacketType {

    // Client -> Server
    IN_CHAT_MESSAGE(0),
    IN_CONSOLE_COMMAND(0),
    IN_KEEP_ALIVE(0),
    IN_LOGIN(0),
    IN_PLAYER_MOVE(0),
    IN_PLAYER_TELEPORT(0),
    IN_POSITION(0),

    // Server -> Client
    OUT_BEING_DESPAWN(0),
    OUT_BEING_MOVE(0),
    OUT_BEING_SPAWN(0),
    OUT_CHAT_MESSAGE(0),
    OUT_CONSOLE_MESSAGE(0),
    OUT_PLAYER_TELEPORT(0),
    OUT_LEVEL(0);

    public static PacketType byId(int id) {
        PacketType[] values = values();
        for (int i = 0; i < values.length; i++) {
            if (values[i].id == id) {
                return values[i];
            }
        }
        return null;
    }

    private final int id;

    PacketType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Packet create(String data) {
        return Packet.out(this, data);
    }

}
