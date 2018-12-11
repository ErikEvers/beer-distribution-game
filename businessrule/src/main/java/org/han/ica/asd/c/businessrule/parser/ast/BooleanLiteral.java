package org.han.ica.asd.c.businessrule.parser.ast;

import org.han.ica.asd.c.businessrule.parser.ast.comparison.Expression;

import java.util.Objects;

public class BooleanLiteral extends Expression {
    private boolean value;

    public BooleanLiteral(boolean b) {
        this.value = b;
    }

    public boolean getValue() {
        return this.value;
    }

    @Override
    public void encode(StringBuilder stringBuilder) {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        BooleanLiteral booleanLiteralObject = (BooleanLiteral) o;
        return Objects.equals(this.value, booleanLiteralObject.value);
    }

    @Override
    public BooleanLiteral resolveCondition() {
        return this;
    }
}
