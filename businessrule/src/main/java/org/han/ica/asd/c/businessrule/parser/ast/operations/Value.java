package org.han.ica.asd.c.businessrule.parser.ast.operations;

import org.han.ica.asd.c.gamevalue.GameValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Value extends OperationValue {
    private static final String PREFIX = "V(";
    private List<String> value = new ArrayList<>();

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
        this.value.add(String.valueOf(value));
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
            this.value.add("lowest");
        } else if ("biggest".equals(value) || "highest".equals(value)) {
            this.value.add("highest");
        }else{
	        this.value.add(value);
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
        stringBuilder.append(PREFIX);
        for (int i = 0; i < value.size(); i++) {
            if(i > 0){
                stringBuilder.append(" ");
            }
            stringBuilder.append(value.get(i));
        }
        stringBuilder.append(SUFFIX);
    }

    /**
     * Getter
     *
     * @return Returns the value
     */
    public List<String> getValue() {
        return this.value;
    }

    /***
     * gets the second part of the variable
     * @return
     */
    public String getSecondPartVariable(){
       return value.get(1);
    }

    /***
     * gets the first part of the variable
     * @return
     */
    public String getFirstPartVariable(){
	    return value.get(0);
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
    public void replaceValueWithValue(String gameValue) {
        if(hasNotBeenReplaced(value.get(0))) {
            if(value.size()>1) {
                if (checkIfFacility(value.get(1))) {
                    value.remove(value.get(1));
                    value.set(0, gameValue);
                    return;
                }
            }
            value.set(0, gameValue);
        }else if(hasNotBeenReplaced(value.get(1))){
            value.set(1,gameValue);
        }
    }
    /***
     * if the variable is a facility then it returns true
     * @param variable one part of the value
     * @return the corresponding game value
     */
    private boolean checkIfFacility(String variable){
        for(GameValue gameValue: GameValue.values()){
            if(gameValue.contains(variable)&&isFacility(gameValue)){
                return true;
            }
        }
        return false;
    }

    /***
     * checks if the game value is of type facility
     * @param gameValue
     * @return
     */
    private boolean isFacility(GameValue gameValue){
        return gameValue==GameValue.FACTORY||
                gameValue==GameValue.WHOLESALER||
                gameValue==GameValue.DISTIRBUTOR||
                gameValue==GameValue.RETAILER;
    }
    /**
     * checks if the value has not been replaced with just a number
     * @param value the value
     * @return
     */
    private boolean hasNotBeenReplaced(String value){
        return !value.matches("[0-9]+")&&!value.matches("[0-9%]+");
    }
}
