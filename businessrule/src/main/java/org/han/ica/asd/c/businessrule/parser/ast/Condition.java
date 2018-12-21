package org.han.ica.asd.c.businessrule.parser.ast;

public abstract class Condition extends ASTNode {
    /**
     * Resolves the {@link Condition} to a single {@link BooleanLiteral}
     *
     * @return Return the {@link BooleanLiteral} that resolves from the Condition
     */
    public abstract BooleanLiteral resolveCondition();
}
