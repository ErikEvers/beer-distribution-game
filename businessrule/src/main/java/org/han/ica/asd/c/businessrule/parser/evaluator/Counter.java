package org.han.ica.asd.c.businessrule.parser.evaluator;

public class Counter {
    private int countedValue;

    public Counter() {
        this.countedValue = 0;
    }

    public void addOne() {
        this.countedValue++;
    }

    int getCountedValue() {
        return this.countedValue;
    }
}
