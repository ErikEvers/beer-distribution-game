package org.han.ica.asd.c.businessrule.ast;

import java.util.Objects;

public class ActionReference extends ASTNode {
    private String action;

    public ActionReference(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return action + "(";
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(action);
    }
}
