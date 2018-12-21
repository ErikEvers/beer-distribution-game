package org.han.ica.asd.c.model.domain_objects;

public class FacilityTurnOrder {
	private int facilityId;
	private int facilityIdOrderTo;
	private int orderAmount;

	public FacilityTurnOrder(int facilityId, int facilityIdOrderTo, int orderAmount, int roundId) {
		this.facilityId = facilityId;
		this.facilityIdOrderTo = facilityIdOrderTo;
		this.orderAmount = orderAmount;
	}

	public int getFacilityIdOrderTo() {
		return facilityIdOrderTo;
	}

	public void setFacilityIdOrderTo(int facilityIdOrderTo) {
		this.facilityIdOrderTo = facilityIdOrderTo;
	}

	public int getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(int orderAmount) {
		this.orderAmount = orderAmount;
	}

	public int getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(int facilityId) {
		this.facilityId = facilityId;
	}
}
