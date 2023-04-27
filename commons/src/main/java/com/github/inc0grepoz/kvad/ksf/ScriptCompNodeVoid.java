package com.github.inc0grepoz.kvad.ksf;

import com.github.inc0grepoz.kvad.ksf.var.Var;

public class ScriptCompNodeVoid extends ScriptCompNode {

//  final Queue<Access> qXcs = new LinkedList<>();
    String varName;

    @Override
    void execute(VarPool varPool) {
        Var var = varPool.get(varName);
        
    }

}
