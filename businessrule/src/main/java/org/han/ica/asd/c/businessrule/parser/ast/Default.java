package org.han.ica.asd.c.businessrule.parser.ast;

public class Default extends Condition {
    private String prefix = "D()";

    @Override
    public String toString() {
        return prefix;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() && super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
