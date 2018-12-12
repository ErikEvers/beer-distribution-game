package org.han.ica.asd.c.businessrule.parser.ast.operators;

import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;

import java.util.Objects;

public abstract class Operator extends ASTNode {
    String operatorVal;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        Operator operator = (Operator) o;
        return Objects.equals(operatorVal, operator.operatorVal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operatorVal);
    }
}
