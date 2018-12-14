package org.han.ica.asd.c.businessrule.parser.ast;

import org.han.ica.asd.c.businessrule.parser.ast.comparison.Expression;

import java.util.Objects;

public class BooleanLiteral extends Expression {
    private boolean value;

    public BooleanLiteral(boolean b) {
        this.value = b;
    }

    /**
     * Gets the boolean value
     *
     * @return Returns the boolean value of the {@link BooleanLiteral}
     */
    public boolean getValue() {
        return this.value;
    }

    /**
     * Not supported for {@link BooleanLiteral}
     *
     * @param stringBuilder Stringbuilder that is used to encode the tree
     */
    @Override
    public void encode(StringBuilder stringBuilder) {
        throw new UnsupportedOperationException();
    }

    /**
     * Resolves the {@link BooleanLiteral} to a single {@link BooleanLiteral}
     *
     * @return Return the {@link BooleanLiteral} that resolves from the {@link BooleanLiteral}
     */
    @Override
    public BooleanLiteral resolveCondition() {
        return this;
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

    /**
     * Hash function used for unit testing
     *
     * @return Returns the hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
