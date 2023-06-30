package com.github.inc0grepoz.kvad.ksf;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScriptTreeNode {

    private static final Pattern QUOTES = Pattern.compile("\"([^\"]*)\"");

    boolean quote, curly, oneChild;
    int brackets;
    String line = new String();

    ScriptTreeNode parent;
    ScriptTreeNodeType type;

    final Queue<ScriptTreeNode> children = new LinkedList<>();

    @Override
    public String toString() {
        String line = this.line + " [" + type.name() + "]\n";

        ScriptTreeNode parent = this.parent;
        while (parent != null) {
            line = parent.line + " -> " + line;
            parent = parent.parent;
        }

        for (ScriptTreeNode node : children) {
            line += node.toString();
        }
        return line;
    }

    ScriptPipe compile_r(ScriptPipe parent) {
        ScriptPipe node = type.compile(this);
        if (parent != null) {
            parent.children.add(node);
            node.parent = parent;
        }
        children.forEach(c -> c.compile_r(node));
        return node;
    }

    Map<String, String> wrapStrings_r(Map<String, String> passedMap) {
        Map<String, String> map = passedMap == null ? new HashMap<>() : passedMap;

        if (!line.isEmpty()) {
            Matcher matcher = QUOTES.matcher(line);
            StringBuffer buff = new StringBuffer();
            String varName, value;

            while (matcher.find()) {
                varName = "wstr_var" + map.size();
                value = matcher.group();
                map.put(varName, value.substring(1, value.length() - 1));
                matcher.appendReplacement(buff, varName);
            }
            matcher.appendTail(buff);
            line = buff.toString();
        }

        children.forEach(c -> c.wrapStrings_r(map));
        return map;
    }

    void clearEmpty() {
        children.removeIf(n -> n.line.isEmpty());
        children.forEach(ScriptTreeNode::clearEmpty);
    }

    void defineTypes_r() {
        if (type == null) {
            type = ScriptTreeNodeType.of(this);
        }
        children.forEach(ScriptTreeNode::defineTypes_r);
    }

    void write(char c) {
        line += c;
    }

    void write(String s) {
        line += s;
    }

    long countChars(char ch) {
        return line.chars().filter(c -> c == ch).count();
    }

    ScriptTreeNode firstScopeMember() {
        ScriptTreeNode first = new ScriptTreeNode();
        first.parent = this;
        children.add(first);
        return first;
    }

    ScriptTreeNode nextScopeMember() {
        ScriptTreeNode next = new ScriptTreeNode();
        next.parent = parent;
        parent.children.add(next);
        return next;
    }

    ScriptTreeNode skipScopeMember() {
        ((LinkedList<ScriptTreeNode>) parent.children).removeLast();
        return nextScopeMember();
    }

}
