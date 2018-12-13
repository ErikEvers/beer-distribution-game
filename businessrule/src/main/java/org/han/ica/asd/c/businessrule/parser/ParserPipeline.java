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
    private List<UserInputBusinessRule> businessRulesInput;
    private List<BusinessRule> businessRulesParsed;
    private Map<String, String> businessRulesMap = new HashMap<>();
    private boolean hasErrors = false;
    private static final String DELETEEMPTYLINES = "(?m)^[ \t]*\r?\n";

    /**
     * Parses the business rules that are provided
     * @param businessRules Business rules that need to be parsed
     */
    public void parseString(List<UserInputBusinessRule> businessRules) {
        this.businessRulesInput = businessRules;
        CharStream inputStream = CharStreams.fromString(userInputToString(businessRules));

        BusinessRuleLexer lexer = new BusinessRuleLexer(inputStream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(ParseErrorListener.INSTANCE);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        BusinessRuleParser parser = new BusinessRuleParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(ParseErrorListener.INSTANCE);

        ParseTree parseTree = parser.businessrules();
        ASTListener listener = new ASTListener();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, parseTree);

        this.businessRulesParsed = listener.getBusinessRules();

        evaluate();
        encodeBusinessRules();
    }

    private String userInputToString(List<UserInputBusinessRule> businessRules) {
        StringBuilder stringBuilder = new StringBuilder();
        for (UserInputBusinessRule businessRule : businessRules) {
            stringBuilder.append(businessRule.getBusinessRule()).append("\n");
        }
        return stringBuilder.toString().replaceAll(DELETEEMPTYLINES, "");
    }

    private void removeEmptyLinesBusinessRulesInput(){
        for (UserInputBusinessRule businessRule : businessRulesInput) {
            if(businessRule.getBusinessRule().isEmpty()){
                businessRulesInput.remove(businessRule);
            }
        }
    }

    /**
     * Encodes the business rules and puts them in a map so that they can be sent and stored in the database.
     */
    private void encodeBusinessRules() {
        for (int i = 0; i < businessRulesParsed.size(); i++) {
            businessRulesMap.put(businessRulesInput.get(i).getBusinessRule(), businessRulesParsed.get(i).encode());
        }
    }

    /**
     * Evaluates the business rules so that they are correct and usable
     */
    private void evaluate() {
        Evaluator evaluator = new Evaluator();
        Map<UserInputBusinessRule,BusinessRule> map = new LinkedHashMap<>();
        for (int i = 0; i < businessRulesParsed.size(); i++) {
            map.put(businessRulesInput.get(i), businessRulesParsed.get(i));
        }

        this.hasErrors = evaluator.evaluate(map);
    }

    /**
     * Getter
     * @return Returns the business rules map that is stored in the database
     */
    public Map<String, String> getBusinessRulesMap() {
        return businessRulesMap;
    }

    public List<UserInputBusinessRule> getBusinessRulesInput() {
        return businessRulesInput;
    }

    public boolean hasErrors() {
        return hasErrors;
    }
}
