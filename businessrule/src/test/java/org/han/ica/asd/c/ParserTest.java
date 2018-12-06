package org.han.ica.asd.c;

import org.han.ica.asd.c.businessrule.PrototypeLexer;
import org.han.ica.asd.c.businessrule.PrototypeParser;
import org.han.ica.asd.c.businessrule.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.parser.ASTListener;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;

import java.io.*;
import java.util.List;

class ParserTest {

    BusinessRule parseString(String input) {
        CharStream inputStream = CharStreams.fromString(input);
        PrototypeLexer lexer = new PrototypeLexer(inputStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        PrototypeParser parser = new PrototypeParser(tokens);
        parser.setErrorHandler(new BailErrorStrategy());

        //Setup collection of the parse error messages
        BaseErrorListener errorListener = new BaseErrorListener() {
            private String message;

            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                message = msg;
            }

            public String toString() {
                return message;
            }
        };
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);

        //Parse & extract AST
        ASTListener listener = new ASTListener();
        try {
            ParseTree parseTree = parser.businessrule();
            ParseTreeWalker walker = new ParseTreeWalker();
            walker.walk(listener, parseTree);
        } catch (ParseCancellationException e) {
            fail(errorListener.toString());
        }

        return listener.getBusinessRules().get(0);
    }

    @Test
    void testParseBasicRule() {
        BusinessRule sut = parseString("if inventory is 20 then order 30");
        BusinessRule exp = Fixtures.BasicRule();
        assertEquals(exp, sut);
    }

    @Test
    void testParseBasicRuleAnd() {
        BusinessRule sut = parseString("if inventory is 20 and backlog higher than 20 then order 30");
        BusinessRule exp = Fixtures.BasicRuleAND();
        assertEquals(exp, sut);
    }

    @Test
    void testParseBasicRuleOr() {
        BusinessRule sut = parseString("if inventory is 20 or backlog higher than 20 then order 30");
        BusinessRule exp = Fixtures.BasicRuleOR();
        assertEquals(exp, sut);
    }

    @Test
    void testParseRuleCalculate() {
        BusinessRule sut = parseString("if inventory is 20+4 then order 30");
        BusinessRule exp = Fixtures.RuleCalculate();
        assertEquals(exp, sut);
    }

    @Test
    void testParseRuleMultipleCalculate() {
        BusinessRule sut = parseString("if inventory is 20/4-3 then order 30");
        BusinessRule exp = Fixtures.RuleMultipleCalculate();
        assertEquals(exp, sut);
    }

    @Test
    void testParseRuleDefault() {
        BusinessRule sut = parseString("Default order 40");
        BusinessRule exp = Fixtures.RuleDefault();
        assertEquals(exp, sut);
    }

    @Test
    void testParseRulePercentage() {
        BusinessRule sut = parseString("if inventory is higher than 20% of backlog then order 30");
        BusinessRule exp = Fixtures.RulePercentage();
        assertEquals(exp, sut);
    }

    @Test
    void testParseRuleWithRound() {
        BusinessRule sut = parseString("if round is 11 then order 30");
        BusinessRule exp = Fixtures.RuleWithRound();
        assertEquals(exp, sut);
    }
}