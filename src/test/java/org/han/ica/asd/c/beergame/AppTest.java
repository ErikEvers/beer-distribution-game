package org.han.ica.asd.c.beergame;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    private App app;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setup(){
        app = new App();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void returnTrueShouldReturnTrue(){
        assertTrue(app.returnTrue());
    }

    @Test
    public void testSentenceShouldBePrinted(){
        app.printSentence();
        assertEquals("Test sentence\n", outContent.toString());

    }

    @After
    public void tearDown(){
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
}
