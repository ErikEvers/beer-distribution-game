package org.han.ica.asd.c.businessrule.parser.ast.action;

import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;

import java.util.Objects;

public class Person extends ASTNode {
    private static final String prefix = "P(";
    private String personNode;

    /**
     * Constructor
     *
     * @param personNode Person to which action has to be sent
     */
    public Person(String personNode) {
        this.personNode = personNode;
    }

    /**
     * Encodes the parsed tree in a single string so that it can be stored in the database
     *
     * @param stringBuilder Stringbuilder that is used to encode the tree
     */
    @Override
    public void encode(StringBuilder stringBuilder) {
        super.encode(stringBuilder, getChildren(), prefix + personNode, suffix);
    }

    /**
     * Getter
     *
     * @return Returns the action
     */
    public String getPerson() {
        return personNode;
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
        Person person = (Person) o;
        return Objects.equals(personNode, person.personNode);
    }

    /**
     * Hash function used for unit testing
     *
     * @return Returns the hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(personNode);
    }
}