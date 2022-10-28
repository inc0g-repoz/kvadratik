package com.github.inc0grepoz.kvad.utils;

import java.util.Map;

public class Serializer {

    public static Map<String, Object> deserialize(String json) {
        
        return null;
        //new Gson().fromJson(json);
    }

    /*
    protected static Fam deserialize(FamsManager manager, File jsonFile) {
        String string;
        try {
            string = new String(Files.readAllBytes(jsonFile.toPath()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(string).getAsJsonObject();

        String famId = jsonFile.getName().split("\\.")[0];
        Fam fam = new Fam(manager, jsonFile, famId);
        fam.displayName = object.get("displayName").getAsString();
        fam.level = object.get("level").getAsInt();

        JsonObject emeralds = object.get("emeralds").getAsJsonObject();
        for (Entry<String, JsonElement> entry : emeralds.entrySet()) {
            fam.emeralds.put(entry.getKey(), entry.getValue().getAsInt());
        }

        JsonObject kills = object.get("kills").getAsJsonObject();
        for (Entry<String, JsonElement> entry : kills.entrySet()) {
            fam.kills.put(entry.getKey(), entry.getValue().getAsInt());
        }

        fam.leaders = new ArrayList<String>();
        object.getAsJsonArray("leaders").forEach(leader -> fam.leaders.add(leader.getAsString()));

        fam.members = new ArrayList<String>();
        object.getAsJsonArray("members").forEach(member -> fam.members.add(member.getAsString()));

        return fam;
    }
    */

}
