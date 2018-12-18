package org.han.ica.asd.c.businessrule.parser.ast.operations;

import java.util.Objects;

public class Value extends OperationValue {
    private static final String PREFIX = "V(";
    private String value;

    /**
     * Constructor
     */
    public Value() {
    }

    /**
     * Constructor
     *
     * @param value the value to be saved in the object
     */
    public Value(int value) {
        this.value = Integer.toString(value);
    }

    /**
     * Adds a value to the value string
     *
     * @param value Value to be added to the value string
     * @return Returns itself so that it can be used immediately
     */
    @Override
    public Value addValue(String value) {
        if ("smallest".equals(value) || "lowest".equals(value)) {
            value = "lowest";
        } else if ("biggest".equals(value) || "highest".equals(value)) {
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
     *
     * @param stringBuilder Stringbuilder that is used to encode the tree
     */
    @Override
    public void encode(StringBuilder stringBuilder) {
        stringBuilder.append(PREFIX).append(value).append(SUFFIX);
    }

    /**
     * Getter
     *
     * @return Returns the value
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Returns the {@link Integer} representation of the value
     *
     * @return {@link Integer}
     */
    public Integer getIntegerValue() {
        return Integer.parseInt(this.value);
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
        if (!super.equals(o))
            return false;
        Value valueObject = (Value) o;
        return Objects.equals(value, valueObject.value);
    }

    /**
     * Hash function used for unit testing
     *
     * @return Returns the hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    /**
     * replaces the current value with the game value
     *
     * @param gameValue the value of the game
     */
    public void replaceValue(String gameValue) {
        this.value = gameValue;
    }
}
