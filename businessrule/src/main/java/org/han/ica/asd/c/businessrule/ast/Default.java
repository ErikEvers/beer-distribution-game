package org.han.ica.asd.c.businessrule.ast;

public class Default extends Condition {

    @Override
    public String toString() {
        return "D()";
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
