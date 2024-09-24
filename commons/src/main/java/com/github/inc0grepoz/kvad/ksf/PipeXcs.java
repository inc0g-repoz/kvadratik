package com.github.inc0grepoz.kvad.ksf;

public class PipeXcs extends Pipe {

    final Var var; // Likely to be an instance of VarXcs

    PipeXcs(Var var) {
        this.var = var;
    }

    @Override
    boolean execute(VarPool varPool) {
        var.getVar(varPool);
        return true;
    }

}
