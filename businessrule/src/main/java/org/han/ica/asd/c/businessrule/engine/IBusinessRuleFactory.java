package org.han.ica.asd.c.businessrule.engine;

import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;

public interface IBusinessRuleFactory {
    /**
     * Takes the identifier of the business rule script and transforms it into the object of the identifier
     *
     * @param identifier The identifier of the business rule script
     * @return The object which the identifier represents
     */
    ASTNode create(String identifier);
}
