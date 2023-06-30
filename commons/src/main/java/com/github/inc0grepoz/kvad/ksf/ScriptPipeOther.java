package com.github.inc0grepoz.kvad.ksf;

import com.github.inc0grepoz.kvad.utils.Logger;

public class ScriptPipeOther extends ScriptPipe {

    @Override
    boolean execute(VarPool varPool) {
        Logger.error("Tried to execute a non-implemented script pipe");
        return false;
    }

}
