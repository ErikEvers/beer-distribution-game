package org.han.ica.asd.c.businessrule.parser.evaluator;

import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;

import java.util.Objects;

public class Counter {
    private int countedValue;

    public Counter() {
        this.countedValue = 0;
    }

    public void addOne() {
        this.countedValue++;
    }

    public int getCountedValue() {
        return this.countedValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(countedValue);
    }
}
