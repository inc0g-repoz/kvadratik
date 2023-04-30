package com.github.inc0grepoz.kvad.ksf;

import com.github.inc0grepoz.kvad.utils.Logger;

public class ScriptPipeMissing extends ScriptPipe {

    @Override
    void execute(VarPool varPool) {
        Logger.error("Tried to execute a non-implemented script pipe");
    }

}
