package org.han.ica.asd.c.beergame;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        System.out.println(args[0]);

    }

    public boolean returnTrue(){
        return true;
    }

    public void printSentence(){
        System.out.println("Test sentence");
    }

    public void notTestedFunction(){
        System.out.println("Oops, this function is not tested!");
    }
}
