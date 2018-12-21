package org.han.ica.asd.c.businessrule;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNSimulator;
import org.han.ica.asd.c.businessrule.parser.ParseErrorListener;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class ParseErrorListenerTest {

    @Mock
    Recognizer recognizer;

    @Mock
    RecognitionException recognitionException;

    @Test
    void testSyntaxError_ErrorWithExpecting() {
        ParseErrorListener parseErrorListener = new ParseErrorListener();

        parseErrorListener.syntaxError(recognizer, "",1,1,"expecting",recognitionException);
        Map<Integer, Integer> res = parseErrorListener.getWordExceptions();
        Map<Integer, Integer> exp = new HashMap<>();
        exp.put(1,1);

        assertEquals(exp,res);
    }

}
