package org.han.ica.asd.c.businessrule.parser.ast;

import java.util.Objects;

public class Default extends Condition {
    private String prefix = "D()";

    /**
     * Encodes the parsed tree in a single string so that it can be stored in the database
     *
     * @param stringBuilder Stringbuilder that is used to encode the tree
     */
    @Override
    public void encode(StringBuilder stringBuilder) {
        stringBuilder.append(prefix);
    }

    /**
     * Equals function used for unit testing
     *
     * @param o Object that needs to be checked if it's equal to this object
     * @return Returns true or false depending on if it's equal or not
     */
    @Override
    public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() && super.equals(o);
    }

    /**
     * Hash function used for unit testing
     *
     * @return Returns the hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(prefix);
    }

    /**
     * Resolves the {@link BooleanLiteral} to a single {@link BooleanLiteral}
     *
     * @return Return the {@link BooleanLiteral} that resolves from the Default
     */
    @Override
    public BooleanLiteral resolveCondition() {
        return new BooleanLiteral(true);
    }
}
