package org.han.ica.asd.c.businessrule;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.name.Names;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.parser.walker.ASTListener;
import org.han.ica.asd.c.businessrule.stubs.BusinessRuleStoreStub;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRuleStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ParserTest {

    private Provider<ASTListener> astListenerProvider;
    private Provider<Fixtures> fixturesProvider;

    @BeforeEach
    public void setup() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IBusinessRuleStore.class).annotatedWith(Names.named("BusinessruleStore")).to(BusinessRuleStoreStub.class);
            }
        });
        astListenerProvider = injector.getProvider(ASTListener.class);
        fixturesProvider = injector.getProvider(Fixtures.class);
    }

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
        ASTListener listener = astListenerProvider.get();
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
        BusinessRule exp = fixturesProvider.get().BasicRule();
        assertEquals(exp, sut);
    }

    @Test
    void testParseBasicRuleAnd() {
        BusinessRule sut = parseString("if inventory is 20 and backlog higher than 20 then order 30");
        BusinessRule exp = fixturesProvider.get().BasicRuleAND();
        assertEquals(exp, sut);
    }

    @Test
    void testParseBasicRuleOr() {
        BusinessRule sut = parseString("if inventory is 20 or backlog higher than 20 then order 30");
        BusinessRule exp = fixturesProvider.get().BasicRuleOR();
        assertEquals(exp, sut);
    }

    @Test
    void testParseRuleCalculate() {
        BusinessRule sut = parseString("if inventory is 20+4 then order 30");
        BusinessRule exp = fixturesProvider.get().RuleCalculate();
        assertEquals(exp, sut);
    }

    @Test
    void testParseRuleMultipleCalculate() {
        BusinessRule sut = parseString("if inventory is 20/4-3 then order 30");
        BusinessRule exp = fixturesProvider.get().RuleMultipleCalculate();
        assertEquals(exp, sut);
    }

    @Test
    void testParseRuleDefault() {
        BusinessRule sut = parseString("Default order 40");
        BusinessRule exp = fixturesProvider.get().RuleDefault();
        assertEquals(exp, sut);
    }

    @Test
    void testParseRulePercentage() {
        BusinessRule sut = parseString("if inventory is higher than 20% of backlog then order 30");
        BusinessRule exp = fixturesProvider.get().RulePercentage();
        assertEquals(exp, sut);
    }

    @Test
    void testParseRuleWithRound() {
        BusinessRule sut = parseString("if round is 11 then order 30");
        BusinessRule exp = fixturesProvider.get().RuleWithRound();
        assertEquals(exp, sut);
    }
}