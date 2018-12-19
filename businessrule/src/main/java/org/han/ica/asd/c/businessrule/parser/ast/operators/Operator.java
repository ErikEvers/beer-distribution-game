package org.han.ica.asd.c.businessrule.parser.ast.operators;

import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;

import java.util.Objects;

public abstract class Operator extends ASTNode {
    String operatorVal;

    /**
     * Equals function used for unit testing
     * @param o Object that needs to be checked if it's equal to this object
     * @return Returns true or false depending on if it's equal or not
     */
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

    /**
     * Hash function used for unit testing
     * @return Returns the hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(operatorVal);
    }
}
