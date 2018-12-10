package org.han.ica.asd.c.businessrule.parser;


import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.han.ica.asd.c.businessrule.BusinessRuleLexer;
import org.han.ica.asd.c.businessrule.BusinessRuleParser;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.parser.evaluator.Evaluator;
import org.han.ica.asd.c.businessrule.parser.walker.ASTListener;

import java.util.*;

public class ParserPipeline {
    private List<String> businessRulesInput = new ArrayList<>();
    private List<BusinessRule> businessRulesParsed;
    private Map<String, String> businessRulesMap = new HashMap<>();

    public void parseString(String input) {
        input =  input.replaceAll("(?m)^[ \t]*\r?\n", "");
        CharStream inputStream = CharStreams.fromString(input);
        businessRulesInput.addAll(Arrays.asList(inputStream.toString().split("\n")));
        BusinessRuleLexer lexer = new BusinessRuleLexer(inputStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        BusinessRuleParser parser = new BusinessRuleParser(tokens);
        ParseTree parseTree = parser.businessrules();

        ASTListener listener = new ASTListener();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, parseTree);

        this.businessRulesParsed = listener.getBusinessRules();
        evaluate();
        encodeBusinessRules();
    }

    private void encodeBusinessRules() {
        for (int i = 0; i < businessRulesParsed.size(); i++) {
            businessRulesMap.put(businessRulesInput.get(i), businessRulesParsed.get(i).encode());
        }
    }

    private void evaluate() {
        Evaluator evaluator = new Evaluator();
        evaluator.evaluate(businessRulesParsed);
    }

    public Map<String, String> getBusinessRulesMap() {
        return businessRulesMap;
    }

    public List<String> getBusinessRulesInput() {
        return businessRulesInput;
    }
}
