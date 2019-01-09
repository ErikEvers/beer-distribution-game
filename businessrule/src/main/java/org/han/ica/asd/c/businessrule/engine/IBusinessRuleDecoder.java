package org.han.ica.asd.c.businessrule.engine;

import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;

public interface IBusinessRuleDecoder {
    BusinessRule decodeBusinessRule(String businessRuleString);
}
