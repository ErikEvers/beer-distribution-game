package org.han.ica.asd.c.businessrule.parser.ast.operations;

import java.util.Objects;

public class Value extends OperationValue {
    private String prefix = "V(";
    private String suffix = ")";
    private String value;

    /**
     * Adds a value to the value string
     * @param value Value to be added to the value string
     * @return Returns itself so that it can be used immediately
     */
    public Value addValue(String value) {
        if(value.equals("smallest") || value.equals("lowest")){
            value = "lowest";
        } else if (value.equals("biggest") || value.equals("highest")){
            value = "highest";
        }

        if (this.value == null) {
            this.value = value;
        } else {
            this.value += (" " + value);
        }
        return this;
    }

    /**
     * Encodes the parsed tree in a single string so that it can be stored in the database
     * @param stringBuilder Stringbuilder that is used to encode the tree
     */
    @Override
    public void encode(StringBuilder stringBuilder) {
        stringBuilder.append(prefix).append(value).append(suffix);
    }

    /**
     * Getter
     * @return Returns the value
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Equals function used for unit testing
     * @param o Object that needs to be checked if it's equal to this object
     * @return Returns true or false depending on if it's equal or not
     */
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

    /**
     * Hash function used for unit testing
     * @return Returns the hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
