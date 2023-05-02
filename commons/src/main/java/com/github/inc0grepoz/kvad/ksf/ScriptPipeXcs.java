package com.github.inc0grepoz.kvad.ksf;

public class ScriptPipeXcs extends ScriptPipe {

    final Var var; // Likely to be an instance of VarXcs

    ScriptPipeXcs(Var var) {
        this.var = var;
    }

    @Override
    boolean execute(VarPool varPool) {
        var.getVar(varPool);
        return true;
    }

}
