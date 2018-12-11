package org.han.ica.asd.c.businessrule.parser.ast;

import java.util.Objects;

public class ActionReference extends ASTNode {
    private String prefix = "AR(";
    private String suffix = ")";
    private String action;

    public ActionReference() {}
    public ActionReference(String action) {
        this.action = action;
    }

    @Override
    public ActionReference addValue(String value) {
        this.action = value;
        return this;
    }

    @Override
    public void encode(StringBuilder stringBuilder) {
        super.encode(stringBuilder,getChildren(), prefix + action, suffix);
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