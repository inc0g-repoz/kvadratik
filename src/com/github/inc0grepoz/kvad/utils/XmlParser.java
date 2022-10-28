package com.github.inc0grepoz.kvad.utils;

import java.util.ArrayList;
import java.util.HashMap;

public class XmlParser {

    public static HashMap<String, String> readFromString(String xml) {
        HashMap<String, String> map = new HashMap<>();
        StringBuilder value = new StringBuilder();
        ArrayList<String> path = new ArrayList<>();

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

}
