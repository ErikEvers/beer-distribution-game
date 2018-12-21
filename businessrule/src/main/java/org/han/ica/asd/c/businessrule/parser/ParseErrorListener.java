package org.han.ica.asd.c.businessrule.parser;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParseErrorListener extends BaseErrorListener {

    static final ParseErrorListener INSTANCE = new ParseErrorListener();
    private List<Integer> lines = new ArrayList<>();
    private Map<Integer, Integer> lineChar = new HashMap<>();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        if(msg.contains("expecting")){
            lineChar.put(line,charPositionInLine);
        }
        lines.add(line);
    }

    List<Integer> getExceptions() {
        return lines;
    }

    Map<Integer, Integer> getWordExceptions() {
        return lineChar;
    }
}
