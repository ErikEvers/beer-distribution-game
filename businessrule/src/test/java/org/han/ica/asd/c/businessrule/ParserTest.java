package org.han.ica.asd.c.businessrule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.parser.walker.ASTListener;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ParserTest {

    BusinessRule parseString(String input) {
        CharStream inputStream = CharStreams.fromString(input);
        BusinessRuleLexer lexer = new BusinessRuleLexer(inputStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        BusinessRuleParser parser = new BusinessRuleParser(tokens);
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
        Injector injector = Guice.createInjector();
        ASTListener listener = injector.getInstance(ASTListener.class);
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