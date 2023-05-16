package com.github.inc0grepoz.kvad.ksf;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@FunctionalInterface
interface Evaluator {
    VarValue passOperands(VarPool varPool, Var... o);
}

@FunctionalInterface
interface Resolver {
    void resolve(OpTemp temp);
}

class OpTemp {

    boolean hasNext = true;
    String exp;
    VarOp lastItGave;

    OpTemp(String exp) {
        this.exp = exp;
    }

}

public enum Operator {

    N_ADD(
        "+",
        "(\\w*)\\+(\\w*)",
        tmp -> {
            String[] lr = tmp.exp.split("( )*+( )*");
            tmp.lastItGave = new VarOp(null, Expressions.resolveVar(lr[0]), Expressions.resolveVar(lr[1]));
        },
        (vp, o) -> {
            double r = (double) o[1].getValue(vp) + (double) o[2].getValue(vp);
            return new VarValue(r % 1 == 0 ? (int) r : r);
        },
        2
    ),
    N_SUB(
        "-",
        "(\\w*)-(\\w*)",
        tmp -> {},
        (vp, o) -> {
            double r = (double) o[1].getValue(vp) - (double) o[2].getValue(vp);
            return new VarValue(r % 1 == 0 ? (int) r : r);
        },
        2
    ),
    N_MULT(
        "*",
        "(\\w)\\*(\\w)",
        tmp -> {},
        (vp, o) -> {
            double r = (double) o[1].getValue(vp) * (double) o[2].getValue(vp);
            return new VarValue(r % 1 == 0 ? (int) r : r);
        },
        2
    ),
    N_DIV(
        "/",
        "(\\w*)\\/(\\w*)",
        tmp -> {},
        (vp, o) -> {
            double r = (double) o[1].getValue(vp) / (double) o[2].getValue(vp);
            return new VarValue(r % 1 == 0 ? (int) r : r);
        },
        2
    ),
    N_MOD(
        "%",
        "(\\w*)%(\\w*)",
        tmp -> {},
        (vp, o) -> {
            double r = (double) o[1].getValue(vp) % (double) o[2].getValue(vp);
            return new VarValue(r % 1 == 0 ? (int) r : r);
        },
        2
    ),
    B_NOT(
        "!",
        "!(\\w*)",
        tmp -> {},
        (vp, o) -> new VarValue(!(boolean) o[0].getValue(vp)),
        1
    ),
    B_AND(
        "&&",
        "(\\w*)&&(\\w*)",
        tmp -> {},
        (vp, o) -> new VarValue((boolean) o[0].getValue(vp) && (boolean) o[1].getValue(vp)),
        2
    ),
    B_OR(
        "||",
        "(\\w*)\\|\\|(\\w*)",
        tmp -> {},
        (vp, o) -> new VarValue((boolean) o[0].getValue(vp) || (boolean) o[1].getValue(vp)),
        2
    ),
    B_GTR(
        ">",
        "(\\w*)>(\\w*)",
        tmp -> {},
        (vp, o) -> new VarValue((double) o[1].getValue(vp) > (double) o[2].getValue(vp)),
        2
    ),
    B_LSS(
        "<",
        "(\\w*)<(\\w*)",
        tmp -> {},
        (vp, o) -> new VarValue((double) o[1].getValue(vp) < (double) o[2].getValue(vp)),
        2
    ),
    B_EQ(
        "==",
        "(\\w*)==(\\w*)",
        tmp -> {},
        (vp, o) -> new VarValue((double) o[1].getValue(vp) == (double) o[2].getValue(vp)),
        2
    ),
    B_EQ_GTR(
        ">=",
        "(\\w*)>=(\\w*)",
        tmp -> {},
        (vp, o) -> new VarValue((double) o[1].getValue(vp) >= (double) o[2].getValue(vp)),
        2
    ),
    B_EQ_LSS(
        "<=",
        "(\\w*)<=(\\w*)",
        tmp -> {},
        (vp, o) ->  new VarValue((double) o[1].getValue(vp) <= (double) o[2].getValue(vp)),
        2
    ),
    B_TERN(
        "?",
        "(\\w*)?(\\w*):(\\w*)",
        tmp -> {},
        (vp, o) -> new VarValue((boolean) o[0].getValue(vp) ? o[1].getValue(vp) : o[2].getValue(vp)),
        3
    ),
    V_ASG(
        "=",
        "(\\w*)=(\\w*)",
        tmp -> {},
        (vp, o) -> {
            VarValue vv = (VarValue) o[0];
            vv.value = o[1].getValue(vp);
            return vv;
        },
        2
    );

    static Operator find(String exp) {
        OpTemp opTemp = null;
        for (Operator optr : values()) {
            if (!exp.contains(optr.nttn)) {
                continue;
            }

            Matcher m = optr.syntax.matcher(exp);
            StringBuffer buff = new StringBuffer();
            String varName;

            while (m.find()) {
                //varName
                m.appendReplacement(buff, exp);
            }
            m.appendTail(buff);
        }
        return null;
    }

    static Operator find(OpTemp obr) {
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
