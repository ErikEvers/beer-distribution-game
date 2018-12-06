package org.han.ica.asd.c.businessrule;


import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.han.ica.asd.c.businessrule.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.evaluator.Evaluator;
import org.han.ica.asd.c.businessrule.parser.ASTListener;

import java.util.*;

public class Pipeline {
    private List<String> businessRulesInput = new ArrayList<>();
    private List<BusinessRule> businessRulesParsed;
    private Map<String,String> businessRulesMap = new HashMap<>();

    public void parseString(String input) {
        //Lex (with Antlr's generated lexer)
        CharStream inputStream = CharStreams.fromString(input);
        businessRulesInput.addAll(Arrays.asList(inputStream.toString().split("\n")));
        PrototypeLexer lexer = new PrototypeLexer(inputStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        //Parse (with Antlr's generated parser)
        PrototypeParser parser = new PrototypeParser(tokens);
        ParseTree parseTree = parser.businessrules();

        //Extract AST from the Antlr parse tree
        ASTListener listener = new ASTListener();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, parseTree);

        this.businessRulesParsed = listener.getBusinessRules();
        evaluate();
        generate();
    }

    private void generate(){
        for (int i = 0; i < businessRulesInput.size(); i++) {
            businessRulesMap.put(businessRulesInput.get(i),businessRulesParsed.get(i).toString());
        }

        // For testing purposes
        for (BusinessRule businessRule:businessRulesParsed) {
            System.out.println(businessRule.toString());
        }
    }

    private void evaluate(){
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
