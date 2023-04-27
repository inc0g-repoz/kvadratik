package com.github.inc0grepoz.kvad.ksf;

import com.github.inc0grepoz.kvad.ksf.var.VarXcs;

public class ScriptCompNodeVoid extends ScriptCompNode {

    VarXcs varXcs;

    @Override
    void execute(VarPool varPoolCopy) {
        varXcs.getVar(varPoolCopy);
    }

}
