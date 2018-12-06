package org.han.ica.asd.c.businessrule.parser.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BusinessRule extends ASTNode {
    private String prefix = "BR(";
    private String suffix = ")";
    private Condition condition;
    private Action action;

    @Override
    public ASTNode addChild(ASTNode child) {
        if (condition == null) {
            this.condition = (Condition) child;
        } else {
            this.action = (Action) child;
        }
        return this;
    }

    @Override
    public String toString() {
        return prefix + condition.toString() + action.toString() + suffix;
    }

    @Override
    public List<ASTNode> getChildren() {
        List<ASTNode> list = new ArrayList<>();
        list.add(condition);
        list.add(action);
        return list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        BusinessRule businessRule = (BusinessRule) o;
        return Objects.equals(condition, businessRule.condition) &&
                Objects.equals(action, businessRule.action);
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition, action);
    }
}
