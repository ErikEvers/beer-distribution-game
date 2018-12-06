package org.han.ica.asd.c.businessrule.parser.ast.operations;

import java.util.Objects;

public class Value extends OperationValue {
    private String prefix = "V(";
    private String suffix = ")";
    private String value;

    public Value() {
    }

    public Value(String value) {
        this.value = value;
    }

    public void addValue(String value) {
        if (this.value == null) {
            this.value = value;
        } else {
            this.value += (" " + value);
        }
    }

    @Override
    public String toString() {
        return prefix + value + suffix;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        Value valueObject = (Value) o;
        return Objects.equals(value, valueObject.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
