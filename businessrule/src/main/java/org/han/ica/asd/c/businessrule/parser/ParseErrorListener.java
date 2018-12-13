package org.han.ica.asd.c.businessrule.parser;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.ArrayList;
import java.util.List;

public class ParseErrorListener extends BaseErrorListener {

    public static final ParseErrorListener INSTANCE = new ParseErrorListener();
    private List<Integer> lines = new ArrayList();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        lines.add(line);
    }

    public List<Integer> getExceptions(){return  lines;}

    public void setLines(List<Integer> lines) {
        this.lines = lines;
    }
}
