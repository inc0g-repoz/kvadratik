package com.github.inc0grepoz.kvad.ksf;

import com.github.inc0grepoz.kvad.utils.Logger;

public class PipeOther extends Pipe {

    @Override
    boolean execute(VarPool varPool) {
        Logger.error("Tried to execute a non-implemented script pipe");
        return false;
    }

}
