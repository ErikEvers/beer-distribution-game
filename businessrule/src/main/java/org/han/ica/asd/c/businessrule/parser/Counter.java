package org.han.ica.asd.c.businessrule.parser;

import java.util.Objects;

public class Counter {
    private int countedValue;

    /**
     * Constructor
     */
    public Counter() {
        this.countedValue = 0;
    }

    /**
     * Adds one to the countedValue
     */
    public void addOne() {
        this.countedValue++;
    }

    /**
     * Getter
     *
     * @return Returns the counterValue
     */
    public int getCountedValue() {
        return this.countedValue;
    }

    /**
     * Equals function used for unit testing
     *
     * @param o Object that needs to be checked if it's equal to this object
     * @return Returns true or false depending on if it's equal or not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        return super.equals(o);
    }

    /**
     * Hash function used for unit testing
     *
     * @return Returns the hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(countedValue);
    }
}
