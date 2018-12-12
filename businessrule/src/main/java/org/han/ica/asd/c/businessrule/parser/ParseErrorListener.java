package org.han.ica.asd.c.businessrule.parser;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.ArrayList;
import java.util.List;

public class ParseErrorListener extends BaseErrorListener {

    public static final ParseErrorListener INSTANCE = new ParseErrorListener();
    private List<BusinessRuleException> exceptions = new ArrayList<>();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        exceptions.add(new BusinessRuleException(msg,line));
    }

    public List<BusinessRuleException> getExceptions() {
        return exceptions;
    }
}
