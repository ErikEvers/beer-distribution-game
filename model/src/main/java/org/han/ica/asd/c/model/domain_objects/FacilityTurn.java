package org.han.ica.asd.c.model.domain_objects;

public class FacilityTurn {
	private int facilityId;
	private int stock;
	private int remainingBudget;
	private boolean bankrupt;

	public FacilityTurn(int facilityId, int stock, int remainingBudget, boolean bankrupt) {
		this.facilityId = facilityId;
		this.stock = stock;
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
}
