package com.github.inc0grepoz.kvad.ksf;

import java.util.LinkedList;
import java.util.Queue;

public class ScriptTreeNode {

    boolean quote, brace, curly;
    String line = new String();

    ScriptTreeNode parent;
    ScriptTreeNodeType type;
    Queue<ScriptTreeNode> children = new LinkedList<>();

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

    void clearEmpty() {
        children.removeIf(n -> n.line.isEmpty());
        children.forEach(ScriptTreeNode::clearEmpty);
    }

    void defineType() {
        if (type != null) {
            return;
        }
        for (ScriptTreeNodeType type : ScriptTreeNodeType.values()) {
            if (type.test(this)) {
                this.type = type;
                break;
            }
        }
    }

    void defineTypesRecursively() {
        defineType();
        children.forEach(ScriptTreeNode::defineTypesRecursively);
    }

    void write(char c) {
        line += c;
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
