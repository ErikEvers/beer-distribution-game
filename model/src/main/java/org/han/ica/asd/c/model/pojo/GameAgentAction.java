package org.han.ica.asd.c.model.pojo;

public abstract class GameAgentAction {
    private int amount;

    public void setAmount(int amount){
        this.amount = amount;
    }

    public int getAmount(){
        return this.amount;
    }

    public abstract String getType();
}
