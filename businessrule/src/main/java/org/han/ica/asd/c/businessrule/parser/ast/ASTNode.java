package org.han.ica.asd.c.businessrule.parser.ast;

import java.util.ArrayList;
import java.util.List;

public abstract class ASTNode {
    protected static final String SUFFIX = ")";

    /**
     * Overridable function for adding a child to an ASTNode
     *
     * @param child Child that has the be added to the ASTNode
     * @return Returns itself so that it can be used immediately
     */
    public ASTNode addChild(ASTNode child) {
        return this;
    }

    /**
     * Overridable function for adding a value to an ASTNode
     *
     * @param value Value that is added to the ASTNode
     * @return Returns itself so that it can be used immediately
     */
    public ASTNode addValue(String value) {
        return this;
    }

    /**
     * Return the children that are assigned to the ASTNode
     *
     * @return Return the children
     */
    public List<ASTNode> getChildren() {
        return new ArrayList<>();
    }

    /**
     * Encodes the parsed tree in a single string so that it can be stored in the database
     *
     * @param stringBuilder Stringbuilder that is used to encode the tree
     * @param children      Children that it needs to encode into the string as well
     * @param prefix        Prefix to encode before the children.
     */
    public void encode(StringBuilder stringBuilder, List<ASTNode> children, String prefix) {
        stringBuilder.append(prefix);
        for (ASTNode child : children) {
            if (child != null) {
                child.encode(stringBuilder);
            }
        }
        stringBuilder.append(SUFFIX);
    }

    /**
     * Abstract function that all ASTNodes need to implement so that an AST can be encoded and saved in the database
     *
     * @param stringBuilder Stringbuilder that is used to encode the tree
     */
    public abstract void encode(StringBuilder stringBuilder);

    /**
     * Equals function used for unit testing
     *
     * @param o Object that needs to be checked if it's equal to this object
     * @return Returns true or false depending on if it's equal or not
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ASTNode))
            return false;
        List<ASTNode> thisChildren = this.getChildren();
        List<ASTNode> otherChildren = ((ASTNode) o).getChildren();
        if (otherChildren.size() != thisChildren.size())
            return false;
        for (int i = 0; i < thisChildren.size(); i++) {
            if (!thisChildren.get(i).equals(otherChildren.get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Hash function used for unit testing
     *
     * @return Returns the hashcode
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
