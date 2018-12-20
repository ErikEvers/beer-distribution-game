package org.han.ica.asd.c.businessrule.parser.ast.operations;

import org.han.ica.asd.c.businessrule.mocks.GameData;
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
        }
        int countSpaces = countSpaces(value);
        String firstValue = getFirstPartVariable(value);
        String secondValue = getSecondPartVariable(value);
        this.value.add(firstValue);
        if(countSpaces>=1&&firstValue!=secondValue){
            this.value.add(secondValue);
        }
        return this;
    }
    public String getSecondPartVariable(String value){
        String newValue="";
        if(isValid(value)){
            return value;
        }
        while(true) {
            int countSpaces = countSpaces(value);
            newValue = value.substring(value.indexOf(" ", 0)+1, value.length());
            if (!isValid(newValue)) {
                value =value.replaceFirst(" ",  "");
                String tmp = value;
            }else{
                break;
            }
        }
        return newValue;
    }

    public String getFirstPartVariable(String value){
        String newValue="";
        if(isValid(value)){
            return value;
        }
        while(true) {
            int countSpaces = countSpaces(value);
            if(countSpaces==0){
                return value;
            }
            newValue = value.substring(0, value.indexOf(" ", 0));
            if(newValue.contains("%")){
                return newValue;
            }
            if (!isValid(newValue)) {
                value =value.replaceFirst(" ",  "");
                String tmp = value;
            }else{
                break;
            }
        }
        return newValue;
    }
    private boolean isValid(String value){
        for(GameValue gameValue: GameValue.values()){
            if(gameValue.contains(value)){
                return true;
            }
        }
        return false;
    }
    private int countSpaces(String value){
        int space = 0;
        for(int i = 0;i<value.length();i++){
            if(value.charAt(i)==' '){
                space++;
            }
        }
        return space;
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

    public String getSecondPartVariable(){
       return value.get(1);
    }

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
    public void replaceValueWithValue(String gameValue, int part) {
       value.set(part,gameValue);
    }
}
