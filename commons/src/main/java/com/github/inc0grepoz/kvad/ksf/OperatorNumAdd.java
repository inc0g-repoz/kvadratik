package com.github.inc0grepoz.kvad.ksf;

import java.util.stream.Stream;

public class OperatorNumAdd implements Operator {

    @Override
    public VarValue passOperands(VarPool varPool, Var... o) {
        Object[] values = Stream.of(o).map(var -> var.getValue(varPool)).toArray(Object[]::new);
        if (Stream.of(values).anyMatch(v -> v instanceof String)) {
            StringBuilder sb = new StringBuilder();
            for (Object obj : values) {
                sb.append(String.valueOf(obj));
            }
            return new VarValue(sb.toString());
        } else {
            double r = 0;
            for (Object v : values) {
                r += Double.valueOf(String.valueOf(v));
            }
            return new VarValue(r % 1 == 0 ? (Object) (int) r : (Object) r);
        }
    }

}
