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
    private static final String DELETEEMPTYLINES = "(?m)^[ \t]*\r?\n";

    /**
     * Parses the business rules that are provided
     * @param businessRules Business rules that need to be parsed
     */
    public void parseString(String businessRules) {
        businessRules = businessRules.replaceAll(DELETEEMPTYLINES, "");
        CharStream inputStream = CharStreams.fromString(businessRules);
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

    /**
     * Encodes the business rules and puts them in a map so that they can be sent and stored in the database.
     */
    private void encodeBusinessRules() {
        for (int i = 0; i < businessRulesParsed.size(); i++) {
            businessRulesMap.put(businessRulesInput.get(i), businessRulesParsed.get(i).encode());
        }
    }

    /**
     * Evaluates the business rules so that they are correct and usable
     */
    private void evaluate() {
        Evaluator evaluator = new Evaluator();
        evaluator.evaluate(businessRulesParsed);
    }

    /**
     * Getter
     * @return Returns the business rules map that is stored in the database
     */
    public Map<String, String> getBusinessRulesMap() {
        return businessRulesMap;
    }

    /**
     * Getter
     * @return Returns the business rules that were put in parseString function
     */
    public List<String> getBusinessRulesInput() {
        return businessRulesInput;
    }
}
