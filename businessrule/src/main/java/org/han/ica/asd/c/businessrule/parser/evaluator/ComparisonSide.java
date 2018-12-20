package org.han.ica.asd.c.businessrule.parser.evaluator;

public enum ComparisonSide {
    RIGHT(2),
    LEFT(0);

    private int side;

    /**
     * Constructor
     *
     * @param side The evaluator symbol
     */
    ComparisonSide(int side) {
        this.side = side;
    }

    /**
     * Getter
     *
     * @return Returns the evaluator symbol
     */
    public int get() {
        return side;
    }

    public static int getOpposite(int side){
        if(side == RIGHT.get()){
            return LEFT.get();
        } else {
            return RIGHT.get();
        }
    }
}