package org.han.ica.asd.c.businessrule.parser.ast.operations;

import org.han.ica.asd.c.gamevalue.GameValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Value extends OperationValue {
    private static final String PREFIX = "V(";
    private List<String> values = new ArrayList<>();

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
        this.values.add(String.valueOf(value));
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
            this.values.add("lowest");
        } else if ("biggest".equals(value) || "highest".equals(value)) {
            this.values.add("highest");
        } else {
            this.values.add(value);
        }
        return this;
    }

    /**
     * Reset the values of the value list
     *
     * @return Returns itself so that it can be used immediately
     */
    public Value resetValues() {
        values.clear();
        return this;
    }

    /**
     * Encodes the parsed tree in a single string so that it can be stored in the database
     *
     * @param stringBuilder Stringbuilder that is used to encode the tree
     */
    @Override
    public void encode(StringBuilder stringBuilder) {
        stringBuilder
                .append(PREFIX)
                .append(values.get(0));

        for (int i = 1, il = values.size(); i < il; i++) {
            stringBuilder
                    .append(' ')
                    .append(values.get(i));
        }
        stringBuilder.append(SUFFIX);
    }

    /**
     * Getter
     *
     * @return Returns the value
     */
    public List<String> getValue() {
        return this.values;
    }

    /***
     * Gets the second part of the variable
     *
     * @return {@link String}
     */
    public String getSecondPartVariable() {
        return this.values.get(1);
    }

    /***
     * Gets the first part of the variable
     *
     * @return {@link String}
     */
    public String getFirstPartVariable() {
        return this.values.get(0);
    }

    /**
     * Returns the {@link Integer} representation of the value. If representation uses a percentage it will calculate the percentage of the value
     *
     * @return {@link Integer}
     */
    public Integer getIntegerValue() {
        String firstPartVariable = getFirstPartVariable();
        if(firstPartVariable.endsWith("%")) {
            return (int)(Integer.parseInt(firstPartVariable.replaceFirst("%", "")) / 100.0f * Integer.parseInt(getSecondPartVariable()) + 0.5f);
        }

        return Integer.parseInt(firstPartVariable);
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
        return Objects.equals(this.values, valueObject.values);
    }

    /**
     * Hash function used for unit testing
     *
     * @return Returns the hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(values);
    }

    /**
     * Replaces the current value with the game value
     *
     * @param gameValue the value of the game
     */
    public void replaceValueWithValue(String gameValue) {
        if (hasNotBeenReplaced(getFirstPartVariable())) {
            if( this.values.size() > 1 && GameValue.checkIfFacility(getSecondPartVariable())){
                this.values.set(1, gameValue);
                return;
            }else if(this.values.size() > 1){
                this.values.remove(getSecondPartVariable());
            }
            this.values.set(0, gameValue);
        } else if (hasNotBeenReplaced(getSecondPartVariable())) {
            this.values.set(1, gameValue);
        }
    }

    /**
     * Checks if the value has not been replaced with just a number
     *
     * @param value the value
     * @return true if the value does not contain any numbers or percentage. Only characters a-zA-Z need to be replaced
     */
    private boolean hasNotBeenReplaced(String value) {
        return !value.matches("[0-9%]+");
    }
}
