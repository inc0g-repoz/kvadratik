package com.github.inc0grepoz.kvad.ksf;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.github.inc0grepoz.kvad.utils.Logger;

public class SyntaxTree {

    int lineCounter = 1;

    SyntaxTreeNode target = new SyntaxTreeNode() {{
        line = "root";
        type = SyntaxTreeNodeType.ROOT;
    }};

    SyntaxTree(File file) {
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
        target.defineNodeTypes_r();

        Logger.info("Found " + target.children.size() + " member(s)");
        for (SyntaxTreeNode member : target.children) {
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
                target.lineIndex = ++lineCounter;
                if (target.line.startsWith("//")) {
                    target = target.skipScopeMember();
                }
                return;
            }
            case '(': {
                if (!target.quote) {
                    target.brackets++;
                }
                target.write(c);
                return;
            }
            case ')': {
                if (!target.quote) {
                    target.brackets--;
                }
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
                if (target.brackets != 0 || target.quote) {
                    target.write(c);
                } else {
                    target = target.nextScopeMember();
                }
                return;
            }
            default: {
                if (target.brackets == 0 && !target.quote
                        && (c == ' ' || c == '\t' || c == '\r')) {
                    if (!target.line.isEmpty()) {
                        target.write(' ');
                    }
                    return;
                }
                target.write(c);
            }
        }
    }

}
