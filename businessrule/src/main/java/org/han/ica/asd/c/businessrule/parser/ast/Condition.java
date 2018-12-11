package org.han.ica.asd.c.businessrule.parser.ast;

public abstract class Condition extends ASTNode {
    public abstract BooleanLiteral resolveCondition();
}
