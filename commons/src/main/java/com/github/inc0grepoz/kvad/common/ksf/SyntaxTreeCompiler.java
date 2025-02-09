package com.github.inc0grepoz.kvad.common.ksf;

@FunctionalInterface
interface SyntaxTreeCompiler {

    Pipe pass(SyntaxTreeNode tn);

}
