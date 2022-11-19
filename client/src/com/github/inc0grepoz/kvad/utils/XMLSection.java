package com.github.inc0grepoz.kvad.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.IntStream.Builder;
import java.util.stream.Stream;

public class XMLSection {

    protected static Map<String, String> deserialize(String xml) {
        Map<String, String> map = new LinkedHashMap<>();
        StringBuilder value = new StringBuilder();
        List<String> path = new ArrayList<>();

        boolean closeTag = false, readValue = false;

        for (int i = 0; i < xml.length(); i++) {
            char letter = xml.charAt(i);

            if (letter == '<') {
                readValue = false;
                i++;

                if (!path.isEmpty() && value.length() != 0) {
                    String key = String.join(".", path);
                    map.put(key, value.toString());
                    value.delete(0, value.length());
                }

                StringBuilder tagName = new StringBuilder();
                for (;; i++) {
                    letter = xml.charAt(i);
                    if (letter == '/') {
                        closeTag = true;
                        if (!path.isEmpty()) {
                            path.remove(path.size() - 1);
                        }
                        break;
                    } else if (letter == '>') {
                        closeTag = false;
                        path.add(tagName.toString());
                        break;
                    } else {
                        tagName.append(letter);
                    }
                }
            } else if (!closeTag) {
                if (letter == ' ' || letter == '\u0009') {
                    if (!readValue) {
                        continue;
                    }
                }
                readValue = true;
                value.append(letter);
            }
        }

        return map;
    }

    private final Map<String, String> map;
    private List<String> keys;
    private String path;

    protected XMLSection(Map<String, String> map) {
        this.map = map;
    }

    protected XMLSection(String xml) {
        this(deserialize(xml));
    }

    public boolean contains(String key) {
        return map.containsKey(key);
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public String getString(String path) {
        return map.getOrDefault(path, null);
    }

    public boolean getBoolean(String path) {
        return map.getOrDefault(path, "").equals("true")
                ? true : false;
    }

    public int getInt(String path) {
        try {
            return Integer.valueOf(getString(path));
        } catch (NullPointerException | NumberFormatException e) {
            return 0;
        }
    }

    public int[] getIntArray(String path) {
        try {
            Builder b = IntStream.builder();
            String string = getString(path);
            if (string != null) {
                Stream.of(getString(path).split(" ")).forEach(s -> {
                    b.add(Integer.valueOf(s).intValue());
                });
                return b.build().toArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public XMLSection getSection(String path) {
        Map<String, String> map = new LinkedHashMap<>();
        this.map.forEach((k, v) -> {
            if (k.startsWith(path)) {
                map.put(k.substring(path.length() + 1), v);
            }
        });
        XMLSection section = new XMLSection(map);
        section.path = this.path != null ? this.path + "." : "";
        section.path += path;
        return section;
    }

    public List<String> getKeys() {
        if (keys == null) {
            keys = keys();
        }
        return keys;
    }

    private List<String> keys() {
        List<String> keys = new ArrayList<>();
        map.keySet().forEach(key -> {
            if (key.contains(".")) {
                key = key.split("\\.")[0];
            }
            if (!keys.contains(key)) {
                keys.add(key);
            }
        });
        return keys;
    }

}
