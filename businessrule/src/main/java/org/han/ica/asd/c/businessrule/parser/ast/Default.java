package org.han.ica.asd.c.businessrule.parser.ast;

import java.util.Objects;

public class Default extends Condition {
    private String prefix = "D()";

    @Override
    public void encode(StringBuilder stringBuilder) {
        stringBuilder.append(prefix);
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() && super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prefix);
    }

    @Override
    public BooleanLiteral resolveCondition() {
        return new BooleanLiteral(true);
    }
}
