package org.han.ica.asd.c.businessrule.parser.ast.operations;

public enum OperationType {
    ADD("+"),
    DIV("/"),
    MUL("*"),
    SUB("-");

    private String operation;

    OperationType(String operation){
        this.operation = operation;
    }

    public String getOperation(){
        return operation;
    }
}

