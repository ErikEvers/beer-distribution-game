package org.han.ica.asd.c.businessrule.parser;


import com.google.inject.Guice;
import com.google.inject.Injector;
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
    private static final String DELETE_EMPTY_LINES = "(?m)^[ \t]*\r?\n";
    private static final String REGEX_SPLIT_ON_NEW_LINE = "\\r?\\n";
    private static final String REGEX_START_WITH_IF_OR_DEFAULT = "(if|default|If|Default)[A-Za-z 0-9*/+\\-%=<>!]+.";


    /**
     * Parses the business rules that are provided
     *
     * @param businessRules Business rules that need to be parsed
     */
    public boolean parseString(String businessRules) {
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

        Injector injector = Guice.createInjector();
        ASTListener listener = injector.getInstance(ASTListener.class);
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, parseTree);

        this.businessRulesParsed = listener.getBusinessRules();

        if(setSyntaxError()){
            ParseErrorListener.INSTANCE.getExceptions().clear();
            return false;
        }

        if(evaluate()){
            return false;
        }

        encodeBusinessRules();

        return true;
    }

    /**
     * Sets the syntax Errors of a line.
     */
    private boolean setSyntaxError() {
        boolean hasErrors = false;

        for (int i = 0; i < businessRulesInput.size(); i++) {
            if (ParseErrorListener.INSTANCE.getExceptions().contains(i + 1)) {
                businessRulesInput.get(i).setErrorMessage("Input error found on: '" + businessRulesInput.get(i).getBusinessRule() + "'");
                hasErrors = true;
            }
        }

        return hasErrors;
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
    private boolean evaluate() {
        Evaluator evaluator = new Evaluator();
        Counter newLineCounter = new Counter();
        Map<UserInputBusinessRule, BusinessRule> map = new LinkedHashMap<>();

        if (!businessRulesParsed.isEmpty()) {
            for (int i = 0; i < businessRulesInput.size(); i++) {
                if (businessRulesInput.get(i).getBusinessRule().isEmpty() || ParseErrorListener.INSTANCE.getExceptions().contains(i + 1) || !businessRulesInput.get(i).getBusinessRule().matches(REGEX_START_WITH_IF_OR_DEFAULT)) {
                    if(!businessRulesInput.get(i).getBusinessRule().matches(REGEX_START_WITH_IF_OR_DEFAULT)){
                        businessRulesInput.get(i).setErrorMessage("Only legitimate business rules are allowed");
                    }
                    newLineCounter.addOne();
                } else {
                    map.put(businessRulesInput.get(i), businessRulesParsed.get(i - newLineCounter.getCountedValue()));
                }
            }

            return evaluator.evaluate(map);
        }
        return true;
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
}
