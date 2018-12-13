package org.han.ica.asd.c.businessrule.parser;


import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.han.ica.asd.c.businessrule.BusinessRuleLexer;
import org.han.ica.asd.c.businessrule.BusinessRuleParser;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.parser.evaluator.Counter;
import org.han.ica.asd.c.businessrule.parser.evaluator.Evaluator;
import org.han.ica.asd.c.businessrule.parser.walker.ASTListener;

import java.util.*;

public class ParserPipeline {
    private List<UserInputBusinessRule> businessRulesInput;
    private List<BusinessRule> businessRulesParsed;
    private Map<String, String> businessRulesMap = new HashMap<>();
    private boolean hasErrors = false;
    private static final String DELETE_EMPTY_LINES = "(?m)^[ \t]*\r?\n";
    private static final String REGEX_SPLIT_ON_NEW_LINE = "\\r?\\n";
    private static final String REGEX_START_WITH_IF_OR_DEFAULT = "(?i)(if|default)[A-Za-z 0-9]+";


    /**
     * Parses the business rules that are provided
     *
     * @param businessRules Business rules that need to be parsed
     */
    public void parseString(String businessRules) {
        businessRulesInput = stringToBusinessRules(businessRules);
        businessRules = businessRules.replace(DELETE_EMPTY_LINES, "");
        CharStream inputStream = CharStreams.fromString(businessRules);

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
        setSyntaxError();
        evaluate();
        encodeBusinessRules();
        ParseErrorListener.INSTANCE.getExceptions().clear();
    }

    /**
     * Sets the syntax Errors of a line.
     */
    private void setSyntaxError() {
        for (int i = 0; i < businessRulesInput.size(); i++) {
            if (ParseErrorListener.INSTANCE.getExceptions().contains(i + 1)) {
                businessRulesInput.get(i).setErrorMessage("Input error found on '" + businessRulesInput.get(i).getBusinessRule() + "'");
            }
        }
    }

    /**
     * Encodes the business rules and puts them in a map so that they can be sent and stored in the database.
     */
    private void encodeBusinessRules() {
        Counter newLineCounter = new Counter();
        if (!businessRulesParsed.isEmpty()) {
            for (int i = 0; i < businessRulesInput.size(); i++) {
                if (businessRulesInput.get(i).getBusinessRule().isEmpty() || ParseErrorListener.INSTANCE.getExceptions().contains(i + 1) || !businessRulesInput.get(i).getBusinessRule().matches(REGEX_START_WITH_IF_OR_DEFAULT)) {
                    newLineCounter.addOne();
                } else {
                    businessRulesMap.put(businessRulesInput.get(i).getBusinessRule(), businessRulesParsed.get(i - newLineCounter.getCountedValue()).encode());
                }
            }
        }
    }

    /**
     * Evaluates the business rules so that they are correct and usable.
     */
    private void evaluate() {
        Evaluator evaluator = new Evaluator();
        Counter newLineCounter = new Counter();
        Map<UserInputBusinessRule, BusinessRule> map = new LinkedHashMap<>();
        if (!businessRulesParsed.isEmpty()) {
            for (int i = 0; i < businessRulesInput.size(); i++) {
                if (businessRulesInput.get(i).getBusinessRule().isEmpty() || ParseErrorListener.INSTANCE.getExceptions().contains(i + 1) || !businessRulesInput.get(i).getBusinessRule().matches(REGEX_START_WITH_IF_OR_DEFAULT)) {
                    newLineCounter.addOne();
                } else {
                    map.put(businessRulesInput.get(i), businessRulesParsed.get(i - newLineCounter.getCountedValue()));
                }
            }
            this.hasErrors = evaluator.evaluate(map);
        }
    }

    /***
     * Setup the agent to be programed.
     * Makes a list of userInputBusinessRule with the parameters.
     * @param businessRuleUserInput This is a string with all the given business rules
     * @return Gives the result of programming a agent.
     */
    private List<UserInputBusinessRule> stringToBusinessRules(String businessRuleUserInput) {
        String[] lines = businessRuleUserInput.split(REGEX_SPLIT_ON_NEW_LINE);
        List<UserInputBusinessRule> businessRules = new ArrayList<>();
        for (int i = 0; i < lines.length; i++) {
            businessRules.add(new UserInputBusinessRule(lines[i], i + 1) {
            });
        }
        return businessRules;
    }

    /**
     * Getter
     *
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
