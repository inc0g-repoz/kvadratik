package com.github.inc0grepoz.kvad.common.ksf;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyntaxTreeNode {

    private static final Pattern QUOTES = Pattern.compile("\"([^\"]*)\"");

    boolean quote, curly, oneChild;
    int brackets, lineIndex = 1;
    String line = new String();

    SyntaxTreeNode parent, prev, next;
    SyntaxTreeNodeType type;
    Pipe compiled;

    final Queue<SyntaxTreeNode> children = new LinkedList<>();

    @Override
    public String toString() {
        String line = this.line + " [" + lineIndex + "] (" + type.name() + ")\n";

        SyntaxTreeNode parent = this.parent;
        while (parent != null) {
            line = parent.line + " -> " + line;
            parent = parent.parent;
        }

        for (SyntaxTreeNode node : children) {
            line += node.toString();
        }
        return line;
    }

    Pipe compile_r(Pipe parent) {
        compiled = type.compile(this, lineIndex);
        if (parent != null) {
            parent.children.add(compiled);
            compiled.parent = parent;
        }
        children.forEach(c -> c.compile_r(compiled));
        return compiled;
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
        children.forEach(SyntaxTreeNode::clearEmpty);
    }

    void defineNodeTypes_r() {
        if (type == null) {
            type = SyntaxTreeNodeType.of(this);
        }
        children.forEach(SyntaxTreeNode::defineNodeTypes_r);
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

    SyntaxTreeNode firstScopeMember() {
        SyntaxTreeNode first = new SyntaxTreeNode();
        first.parent = this;
        children.add(first);
        return first;
    }

    SyntaxTreeNode nextScopeMember() {
        next = new SyntaxTreeNode();
        next.parent = parent;
        next.prev = this;
        parent.children.add(next);
        return next;
    }

    SyntaxTreeNode skipScopeMember() {
        ((LinkedList<SyntaxTreeNode>) parent.children).removeLast();
        return nextScopeMember();
    }

}
