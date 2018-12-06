package org.han.ica.asd.c.businessrule.ast;

import org.han.ica.asd.c.businessrule.ast.operations.OperationValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Action extends ASTNode {
    private ActionReference actionName;
    private OperationValue operation;

    @Override
    public ASTNode addChild(ASTNode child) {
        if (child instanceof ActionReference) {
            this.actionName = (ActionReference) child;
        } else {
            this.operation = (OperationValue) child;
        }
        return this;
    }

    @Override
    public String toString() {
        return "A(" + actionName.toString() + operation.toString() + "))";
    }

    @Override
    public List<ASTNode> getChildren() {
        List<ASTNode> list = new ArrayList<>();
        list.add(actionName);
        list.add(operation);
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
        Action action = (Action) o;
        return Objects.equals(actionName, action.actionName) &&
                Objects.equals(operation, action.operation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actionName, operation);
    }
}
