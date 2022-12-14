package com.github.inc0grepoz.kvad.protocol;

public enum PacketType {

    // Client -> Server
    CLIENT_CHAT_MESSAGE(1),
    CLIENT_CONSOLE_COMMAND(2),
    CLIENT_KEEP_ALIVE(3),
    CLIENT_LOGIN(4),
    CLIENT_PLAYER_ANIM(5),
    CLIENT_PLAYER_POINT(6),

    // Server -> Client
    SERVER_ASSETS_URL(7),
    SERVER_BEING_ANIM(8),
    SERVER_BEING_DESPAWN(9),
    SERVER_BEING_POINT(10),
    SERVER_BEING_SPAWN(11),
    SERVER_BEING_TELEPORT(12),
    SERVER_BEING_TYPE(13),
    SERVER_CHAT_MESSAGE(14),
    SERVER_CONSOLE_MESSAGE(15),
    SERVER_TRANSFER_CONTROL(16),
    SERVER_LEVEL(17);

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
