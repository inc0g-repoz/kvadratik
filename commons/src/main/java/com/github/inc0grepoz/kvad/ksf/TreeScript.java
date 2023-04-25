package com.github.inc0grepoz.kvad.ksf;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.github.inc0grepoz.kvad.utils.Logger;

public class TreeScript {

    TreeNode target = new TreeNode() {{ line = "root"; }};

    public TreeScript(File file) {
        target = target.firstScopeMember();

        try {
            FileReader in = new FileReader(file);

            int nextChar;
            while ((nextChar = in.read()) != -1) {
                write((char) nextChar);
            }

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (target.parent != null) {
            target = target.parent;
        }

        target.clearEmpty();
        target.defineTypesRecursively();

        Logger.info("Found " + target.children.size() + " members");
        for (TreeNode member : target.children) {
            Logger.info("- " + member.line);
        }
    }

    @Override
    public String toString() {
        return target.toString();
    }

    private void write(char c) {
        switch (c) {
            case '\n': {
                if (target.line.startsWith("//")) {
                    target = target.skipScopeMember();
                }
                return;
            }
            case '(': {
                target.brace = true;
                target.write(c);
                return;
            }
            case ')': {
                target.brace = false;
                target.write(c);
                return;
            }
            case '"': {
                target.quote = !target.quote;
                target.write(c);
                return;
            }
            case '{': {
                if (target.quote) {
                    target.write(c);
                } else {
                    target.curly = true;
                    target = target.firstScopeMember();
                }
                return;
            }
            case '}': {
                if (target.quote) {
                    target.write(c);
                } else {
                    target = target.parent;
                    target.curly = false;
                    target = target.nextScopeMember();
                }
                return;
            }
            case ';': {
                if (target.brace || target.quote) {
                    target.write(c);
                } else {
                    target = target.nextScopeMember();
                }
                return;
            }
            default: {
                if (!target.brace && !target.quote && target.line.isEmpty()
                        && Character.isWhitespace(c)) {
                    return;
                }
                target.write(c);
            }
        }
    }

}
