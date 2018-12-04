package org.han.asd.c;

public class Main {
    public static void main(String[] args) {
        // Start the app from here
        Main main = new Main();
        main.getValue();
    }

    public boolean getValue(){
        boolean value = true;
        value = !value;
        return value;
    }
}
