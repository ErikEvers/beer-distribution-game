package org.han.ica.asd.c.model.pojo;

public abstract class GameAgentAction {
    private int targetFacilityId;
    private int amount;

    public void setAmount(int amount){
        this.amount = amount;
    }

    public int getAmount(){
        return this.amount;
    }

    public abstract String getType();

    public void setTargetFacilityId(int targetFacilityId) {
        this.targetFacilityId = targetFacilityId;
    }

    public int getTargetFacilityId() {
        return targetFacilityId;
    }
}
