package org.han.ica.asd.c.businessrule.parser.ast;

import org.han.ica.asd.c.businessrule.parser.ast.comparison.Expression;

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
}
