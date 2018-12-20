package org.han.ica.asd.c.businessrule.parser.ast.operations;

import org.han.ica.asd.c.gamevalue.GameValue;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;

public class Value extends OperationValue {
    private static final String PREFIX = "V(";
    private ArrayList<String> value;

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
    public Value(String value) {
        addValue(value);
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

       this.addValue(value);
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
    public ArrayList<String> getValue() {
        return this.value;
    }

    public String getSecondPartVariable(){
       return value.get(1).replaceAll(" ","");
    }

    public String getFirstPartVariable(){
	    return value.get(0).replaceAll(" ","");
    }

    /**
     * Returns the {@link Integer} representation of the value
     *
     * @return {@link Integer}
     */
    public Integer getIntegerValue() {
        return Integer.parseInt(this.value.get(0));
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
    public void replaceValueWithValue(String gameValue, int part) {
       value.set(part,gameValue);
    }
}
