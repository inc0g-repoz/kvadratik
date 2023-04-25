package com.github.inc0grepoz.kvad.ksf;

import java.util.LinkedList;
import java.util.Queue;

public class TreeNode {

    boolean quote, brace, curly;
    String line = new String();

    TreeNode parent;
    TreeNodeType type;
    Queue<TreeNode> children = new LinkedList<>();

    @Override
    public String toString() {
        String line = this.line + " [" + type.name() + "]\n";

        TreeNode parent = this.parent;
        while (parent != null) {
            line = parent.line + " -> " + line;
            parent = parent.parent;
        }

        for (TreeNode node : children) {
            line += node.toString();
        }
        return line;
    }

    public CompNode compileRecursively() {
        CompNode node = type.compile(null);
        children.forEach(c -> c.compile(node));
        return node;
    }

    public CompNode compile(CompNode parent) {
        CompNode node = type.compile(this);
        parent.children.add(node);
        node.parent = parent;
        return node;
    }

    public void clearEmpty() {
        children.removeIf(n -> n.line.isEmpty());
        children.forEach(TreeNode::clearEmpty);
    }

    public void defineType() {
        for (TreeNodeType type : TreeNodeType.values()) {
            if (type.test(this)) {
                this.type = type;
                break;
            }
        }
    }

    public void defineTypesRecursively() {
        defineType();
        children.forEach(TreeNode::defineTypesRecursively);
    }

    TreeNode firstScopeMember() {
        TreeNode first = new TreeNode();
        first.parent = this;
        children.add(first);
        return first;
    }

    TreeNode nextScopeMember() {
        TreeNode next = new TreeNode();
        next.parent = parent;
        parent.children.add(next);
        return next;
    }

    TreeNode skipScopeMember() {
        ((LinkedList<TreeNode>) parent.children).removeLast();
        return nextScopeMember();
    }

    void write(char c) {
        line += c;
    }

}
