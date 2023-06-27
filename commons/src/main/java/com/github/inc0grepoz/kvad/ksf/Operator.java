package com.github.inc0grepoz.kvad.ksf;

import java.util.regex.Pattern;

import com.github.inc0grepoz.kvad.utils.Logger;

@FunctionalInterface
interface Evaluator {
    VarValue passOperands(VarPool varPool, Var... o);
}

@FunctionalInterface
interface Resolver {
    VarOp resolve(String temp);
}

public enum Operator {

    /*
    BKTS(
        "(",
        "",
        null,
        null,
        1
    ),
    */
    N_ADD(
        "+",
        "(\\w*)\\+(\\w*)",
        tmp -> {
            String[] lr = tmp.split("( )*+( )*");
            return new VarOp(Expressions.resolveVar(lr[0]), Expressions.resolveVar(lr[1]));
        },
        (vp, o) -> {
            String d1s = String.valueOf(o[0].getValue(vp));
            String d2s = String.valueOf(o[1].getValue(vp));
            double r = Double.valueOf(d1s) + Double.valueOf(d2s);
            return new VarValue(r % 1 == 0 ? (int) r : r);
        },
        2
    ),
    N_SUB(
        "-",
        "(\\w*)-(\\w*)",
        tmp -> {
            String[] lr = tmp.split("( )*-( )*");
            return new VarOp(Expressions.resolveVar(lr[0]), Expressions.resolveVar(lr[1]));
        },
        (vp, o) -> {
            String d1s = String.valueOf(o[0].getValue(vp));
            String d2s = String.valueOf(o[1].getValue(vp));
            double r = Double.valueOf(d1s) - Double.valueOf(d2s);
            return new VarValue(r % 1 == 0 ? (int) r : r);
        },
        2
    ),
    N_MULT(
        "*",
        "(\\w)\\*(\\w)",
        tmp -> {
            String[] lr = tmp.split("( )*\\*( )*");
            return new VarOp(Expressions.resolveVar(lr[0]), Expressions.resolveVar(lr[1]));
        },
        (vp, o) -> {
            String d1s = String.valueOf(o[0].getValue(vp));
            String d2s = String.valueOf(o[1].getValue(vp));
            double r = Double.valueOf(d1s) * Double.valueOf(d2s);
            return new VarValue(r % 1 == 0 ? (int) r : r);
        },
        2
    ),
    N_DIV(
        "/",
        "(\\w*)\\/(\\w*)",
        tmp -> {
            String[] lr = tmp.split("( )*\\/( )*");
            return new VarOp(Expressions.resolveVar(lr[1]), Expressions.resolveVar(lr[2]));
        },
        (vp, o) -> {
            String d1s = String.valueOf(o[0].getValue(vp));
            String d2s = String.valueOf(o[1].getValue(vp));
            double r = Double.valueOf(d1s) / Double.valueOf(d2s);
            return new VarValue(r % 1 == 0 ? (int) r : r);
        },
        2
    ),
    N_MOD(
        "%",
        "(\\w*)%(\\w*)",
        tmp -> {
            String[] lr = tmp.split("( )*%( )*");
            return new VarOp(Expressions.resolveVar(lr[1]), Expressions.resolveVar(lr[2]));
        },
        (vp, o) -> {
            String d1s = String.valueOf(o[0].getValue(vp));
            String d2s = String.valueOf(o[1].getValue(vp));
            double r = Double.valueOf(d1s) % Double.valueOf(d2s);
            return new VarValue(r % 1 == 0 ? (int) r : r);
        },
        2
    ),
    B_NOT(
        "!",
        "!(\\w*)",
        tmp -> new VarOp(Expressions.resolveVar(tmp.substring(1))),
        (vp, o) -> new VarValue(!(boolean) o[0].getValue(vp)),
        1
    ),
    B_AND(
        "&&",
        "(\\w*)&&(\\w*)",
        tmp -> {
            String[] lr = tmp.split("( )*&&( )*");
            return new VarOp(Expressions.resolveVar(lr[0]), Expressions.resolveVar(lr[1]));
        },
        (vp, o) -> new VarValue((boolean) o[0].getValue(vp) && (boolean) o[1].getValue(vp)),
        2
    ),
    B_OR(
        "||",
        "(\\w*)\\|\\|(\\w*)",
        tmp -> {
            String[] lr = tmp.split("( )*\\|\\|( )*");
            return new VarOp(Expressions.resolveVar(lr[0]), Expressions.resolveVar(lr[1]));
        },
        (vp, o) -> new VarValue((boolean) o[0].getValue(vp) || (boolean) o[1].getValue(vp)),
        2
    ),
    B_GTR(
        ">",
        "(\\w*)>(\\w*)",
        tmp -> {
            String[] lr = tmp.split("( )*>( )*");
            return new VarOp(Expressions.resolveVar(lr[0]), Expressions.resolveVar(lr[1]));
        },
        (vp, o) -> new VarValue((double) o[1].getValue(vp) > (double) o[2].getValue(vp)),
        2
    ),
    B_LSS(
        "<",
        "(\\w*)<(\\w*)",
        tmp -> {
            String[] lr = tmp.split("( )*<( )*");
            return new VarOp(Expressions.resolveVar(lr[0]), Expressions.resolveVar(lr[1]));
        },
        (vp, o) -> new VarValue((double) o[1].getValue(vp) < (double) o[2].getValue(vp)),
        2
    ),
    B_EQ(
        "==",
        "(\\w*)==(\\w*)",
        tmp -> {
            String[] lr = tmp.split("( )*==( )*");
            return new VarOp(Expressions.resolveVar(lr[0]), Expressions.resolveVar(lr[1]));
        },
        (vp, o) -> new VarValue(o[0].getValue(vp).equals(o[1].getValue(vp))),
        2
    ),
    B_EQ_N(
        "!=",
        "(\\w*)!=(\\w*)",
        tmp -> {
            String[] lr = tmp.split("( )*!=( )*");
            return new VarOp(Expressions.resolveVar(lr[0]), Expressions.resolveVar(lr[1]));
        },
        (vp, o) -> new VarValue(!o[0].getValue(vp).equals(o[1].getValue(vp))),
        2
    ),
    B_EQ_GTR(
        ">=",
        "(\\w*)>=(\\w*)",
        tmp -> {
            String[] lr = tmp.split("( )*>=( )*");
            return new VarOp(Expressions.resolveVar(lr[0]), Expressions.resolveVar(lr[1]));
        },
        (vp, o) -> {
            String d1s = String.valueOf(o[0].getValue(vp));
            String d2s = String.valueOf(o[1].getValue(vp));
            return new VarValue(Double.valueOf(d1s) >= Double.valueOf(d2s));
        },
        2
    ),
    B_EQ_LSS(
        "<=",
        "(\\w*)<=(\\w*)",
        tmp -> {
            String[] lr = tmp.split("( )*<=( )*");
            return new VarOp(Expressions.resolveVar(lr[0]), Expressions.resolveVar(lr[1]));
        },
        (vp, o) -> {
            String d1s = String.valueOf(o[0].getValue(vp));
            String d2s = String.valueOf(o[1].getValue(vp));
            return new VarValue(Double.valueOf(d1s) <= Double.valueOf(d2s));
        },
        2
    ),
    B_TERN(
        "?",
        "(\\w*)\\?(\\w*):(\\w*)",
        tmp -> {
            String[] lr = tmp.split("( )*(\\?|:)( )*");
            return new VarOp(Expressions.resolveVar(lr[0]), Expressions.resolveVar(lr[1]), Expressions.resolveVar(lr[2]));
        },
        (vp, o) -> new VarValue((boolean) o[0].getValue(vp) ? o[1].getValue(vp) : o[2].getValue(vp)),
        3
    ),
    V_ASG(
        "=",
        "(\\w*)=(\\w*)",
        tmp -> {
            String[] lr = tmp.split("( )=( )*");
            return new VarOp(Expressions.resolveVar(lr[0]), Expressions.resolveVar(lr[1]));
        },
        (vp, o) -> {
            VarValue vv = (VarValue) o[0];
            vv.value = o[1].getValue(vp);
            return vv;
        },
        2
    );

    static Operator find(String codeLine) {
        for (Operator enumOp : Operator.values()) {
            if (codeLine.contains(enumOp.nttn)) {
                return enumOp;
            }
        }
        return null;
    }

    final String nttn;    // Notation
    final Pattern syntax; // Syntax template
    final Resolver reslv; // Syntax resolver
    final Evaluator eval; // Implementation
    final int oc;         // Operands count

    Operator(String nttn, String syntax, Resolver reslv, Evaluator eval, int operands) {
        this.nttn = nttn;
        this.syntax = Pattern.compile(syntax);
        this.reslv = reslv;
        this.eval = eval;
        this.oc = operands;
    }

    VarValue eval(VarPool varPool, Var... o) {
        return eval.passOperands(varPool, o);
    }

}
