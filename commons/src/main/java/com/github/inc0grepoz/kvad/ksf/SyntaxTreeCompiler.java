package com.github.inc0grepoz.kvad.ksf;

@FunctionalInterface
interface SyntaxTreeCompiler {

    Pipe pass(SyntaxTreeNode tn);

}
