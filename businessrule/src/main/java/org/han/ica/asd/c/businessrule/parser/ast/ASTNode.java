package org.han.ica.asd.c.businessrule.parser.ast;

import java.util.ArrayList;
import java.util.List;

public abstract class ASTNode {
    public ASTNode addChild(ASTNode child) {
        return this;
    }

    public List<ASTNode> getChildren() {
        return new ArrayList<>();
    }

    public void encode(StringBuilder stringBuilder, List<ASTNode> children, String prefix, String suffix) {
        stringBuilder.append(prefix);
        for (ASTNode child : children) {
            if(child != null){
                child.encode(stringBuilder);
            }
        }
        stringBuilder.append(suffix);
    }

    public abstract void encode(StringBuilder stringBuilder);

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

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
