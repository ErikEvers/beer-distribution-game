package org.han.ica.asd.c.model.domain_objects;

public class FacilityTurn {
	private int facilityId;
	private int roundId;
	private int stock;
	private int remainingBudget;
	private int backorders;
	private boolean bankrupt;

	public FacilityTurn(int facilityId, int roundId, int stock, int backorders, int remainingBudget, boolean bankrupt) {
		this.facilityId = facilityId;
		this.roundId = roundId;
		this.stock = stock;
		this.backorders = backorders;
		this.remainingBudget = remainingBudget;
		this.bankrupt = bankrupt;
	}

	public int getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(int facilityId) {
		this.facilityId = facilityId;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getBackorders() {
		return backorders;
	}

	public void setBackorders(int backorders) {
		this.backorders = backorders;
	}

	public int getRemainingBudget() {
		return remainingBudget;
	}

	public void setRemainingBudget(int remainingBudget) {
		this.remainingBudget = remainingBudget;
	}

	public boolean isBankrupt() {
		return bankrupt;
	}

	public void setBankrupt(boolean bankrupt) {
		this.bankrupt = bankrupt;
	}

	public int getRoundId() {
		return roundId;
	}

	public void setRoundId(int roundId) {
		this.roundId = roundId;
	}
}
