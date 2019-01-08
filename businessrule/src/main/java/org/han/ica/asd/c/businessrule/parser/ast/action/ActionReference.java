package org.han.ica.asd.c.businessrule.parser.ast.action;

import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;

import java.util.Objects;

public class ActionReference extends ASTNode {
    private static final String PREFIX = "AR(";
    private String action;

    /**
     * Constructor
     */
    public ActionReference() {
    }

    /**
     * Constructor
     *
     * @param action The action that has to be executed for this businessrule
     */
    public ActionReference(String action) {
        this.action = action;
    }


    @Override
    public ActionReference addValue(String value) {
        this.action = value;
        return this;
    }

    /**
     * Encodes the parsed tree in a single string so that it can be stored in the database
     *
     * @param stringBuilder Stringbuilder that is used to encode the tree
     */
    @Override
    public void encode(StringBuilder stringBuilder) {
        super.encode(stringBuilder, getChildren(), PREFIX + action, SUFFIX);
    }

    /**
     * Getter
     *
     * @return Returns the action
     */
    public String getAction() {
        return action;
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
        ActionReference actionReference = (ActionReference) o;
        return Objects.equals(action, actionReference.action);
    }

    /**
     * Hash function used for unit testing
     *
     * @return Returns the hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(action);
    }
}
