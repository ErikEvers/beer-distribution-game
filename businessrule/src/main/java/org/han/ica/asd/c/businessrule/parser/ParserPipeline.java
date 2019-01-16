package org.han.ica.asd.c.businessrule.parser;


import com.google.inject.Inject;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.han.ica.asd.c.businessrule.BusinessRuleLexer;
import org.han.ica.asd.c.businessrule.BusinessRuleParser;
import org.han.ica.asd.c.businessrule.parser.alternatives.AlternativeFinder;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.parser.evaluator.Evaluator;
import org.han.ica.asd.c.businessrule.parser.walker.ASTListener;
import org.han.ica.asd.c.model.interface_models.UserInputBusinessRule;

import javax.inject.Provider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ParserPipeline {
    private List<UserInputBusinessRule> businessRulesInput;
    private List<BusinessRule> businessRulesParsed;
    private Map<String, String> businessRulesMap = new HashMap<>();
    private static final String DELETE_EMPTY_LINES = "(?m)^[ \t]*\r?\n";
    private static final String REGEX_SPLIT_ON_NEW_LINE = "\\r?\\n";
    private static final String REGEX_START_WITH_IF_OR_DEFAULT = "(if|default|If|Default)[A-Za-z 0-9*/+\\-%=<>!\\t]+.";

    private Provider<Counter> counterProvider;
    private Provider<Evaluator> evaluatorProvider;
    private Provider<AlternativeFinder> alternativeFinderProvider;
    private Provider<ASTListener> astListenerProvider;
    private AlternativeFinder alternativeFinder;

    @Inject
    public ParserPipeline(Provider<ASTListener> astListenerProvider, Provider<Counter> counterProvider, Provider<Evaluator> evaluatorProvider, Provider<AlternativeFinder> alternativeFinderProvider) {
        this.counterProvider = counterProvider;
        this.evaluatorProvider = evaluatorProvider;
        this.alternativeFinderProvider = alternativeFinderProvider;
        this.astListenerProvider = astListenerProvider;
        alternativeFinder = this.alternativeFinderProvider.get();
    }

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

        ASTListener listener = astListenerProvider.get();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, parseTree);

        this.businessRulesParsed = listener.getBusinessRules();

        if(setSyntaxError()){
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
        final int lineOffset = 1;

        for (int i = 0; i < businessRulesInput.size(); i++) {
            String businessRule = businessRulesInput.get(i).getBusinessRule();

            if(!businessRule.matches(REGEX_START_WITH_IF_OR_DEFAULT)){
                break;
            }

            if(ParseErrorListener.INSTANCE.getWordExceptions().containsKey(i + lineOffset)){
                int beginError = ParseErrorListener.INSTANCE.getWordExceptions().get(i + lineOffset);
                int endError = findEndError(businessRule,beginError);

                String errorWord = findWordInBusinessRule(businessRule, beginError, endError);
                String errorMessage = "Input error found on: '" + errorWord + "'.";
                String alternative = alternativeFinder.findAlternative(errorWord);

                businessRulesInput.get(i).setErrorMessage(extendErrorMessageWithAlternative(errorMessage, alternative));
                businessRulesInput.get(i).setErrorWord(beginError, endError);
                hasErrors = true;
            } else if (ParseErrorListener.INSTANCE.getExceptions().contains(i + lineOffset)) {
                businessRulesInput.get(i).setErrorMessage("Input error found on: '" + businessRule + "'");
                hasErrors = true;
            }
        }

        clearParseErrorListener();
        return hasErrors;
    }

    private void clearParseErrorListener(){
        ParseErrorListener.INSTANCE.getExceptions().clear();
        ParseErrorListener.INSTANCE.getWordExceptions().clear();
    }

    private String extendErrorMessageWithAlternative(String errorMessage, String alternative){
        StringBuilder builder = new StringBuilder();
        builder.append(errorMessage);
        if (!alternative.isEmpty()){
            builder.append(" Did you mean: '")
                    .append(alternative)
                    .append("'?");
        } else {
            builder.append(" No alternatives found.");
        }

        return builder.toString();
    }
    private String findWordInBusinessRule(String businessRule, int beginChar, int endChar){
        return businessRule.substring(beginChar,endChar + 1);
    }

    private int findEndError(String businessRule, int charPosition){
        if(charPosition > businessRule.length() - 1){
            return charPosition - 1;
        }

        if(" ".equals(String.valueOf(businessRule.charAt(charPosition)))){
            return charPosition - 1;
        }

        return findEndError(businessRule,charPosition+1);
    }

    /**
     * Encodes the business rules and puts them in a map so that they can be sent and stored in the database.
     */
    private void encodeBusinessRules() {
        Counter newLineCounter = counterProvider.get();
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
        Evaluator evaluator = evaluatorProvider.get();
        Counter newLineCounter = counterProvider.get();
        Map<UserInputBusinessRule, BusinessRule> map = new LinkedHashMap<>();
        boolean hasErrors = false;

        if (!businessRulesParsed.isEmpty()) {
            for (int i = 0; i < businessRulesInput.size(); i++) {
                if (businessRulesInput.get(i).getBusinessRule().isEmpty() || ParseErrorListener.INSTANCE.getExceptions().contains(i + 1) || !businessRulesInput.get(i).getBusinessRule().matches(REGEX_START_WITH_IF_OR_DEFAULT)) {
                    hasErrors = setErrorsBusinessrules(hasErrors, i);
                    newLineCounter.addOne();
                } else {
                    map.put(businessRulesInput.get(i), businessRulesParsed.get(i - newLineCounter.getCountedValue()));
                }
            }
            return evaluator.evaluate(map) || hasErrors;
        } else {
            for (UserInputBusinessRule input : businessRulesInput) {
                input.setErrorMessage("Only legitimate business rules are allowed. They start with 'default' or 'if'");
            }
            return true;
        }
    }

    private boolean setErrorsBusinessrules(boolean hasErrors, int i) {
        if(!businessRulesInput.get(i).getBusinessRule().matches(REGEX_START_WITH_IF_OR_DEFAULT)){
            businessRulesInput.get(i).setErrorMessage("Only legitimate business rules are allowed");
            return true;
        }
        return hasErrors;
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
            businessRules.add(new UserInputBusinessRule(lines[i], i + 1));
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
