package com.github.inc0grepoz.kvad.ksf;

import com.github.inc0grepoz.kvad.ksf.var.Var;

public class ScriptPipeVoid extends ScriptPipe {

    Var var; // Likely to be an instance of VarXcs

    @Override
    void execute(VarPool varPoolCopy) {
        var.getVar(varPoolCopy);
    }

}
