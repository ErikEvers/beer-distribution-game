package org.han.ica.asd.c.businessrule.parser.ast;

import java.util.Objects;

public class ActionReference extends ASTNode {
    private String suffix = "(";
    private String action;

    public ActionReference(String action) {
        this.action = action;
    }

    public void encode(StringBuilder stringBuilder) {
       stringBuilder.append(action).append(suffix);
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
