package org.han.ica.asd.c.businessrule.parser.ast.operations;

public enum OperationType {
    ADD("+"),
    DIV("/"),
    MUL("*"),
    SUB("-");

    private String operation;

    /**
     * Constructor
     * @param operation Operation symbol that is used
     */
    OperationType(String operation){
        this.operation = operation;
    }

    /**
     * Getter
     * @return Returns the operation symbol
     */
    public String getOperation(){
        return operation;
    }
}

