package org.han.ica.asd.c.model.domain_objects;

public class FacilityTurnDeliver {
	int facilityId;
	int facilityIdDeliverTo;
	int openOrderAmount;
	int outGoingGoods;

	public FacilityTurnDeliver(int facilityId, int facilityIdDeliverTo, int openOrderAmount, int outGoingGoods) {
		this.facilityId = facilityId;
		this.facilityIdDeliverTo = facilityIdDeliverTo;
		this.openOrderAmount = openOrderAmount;
		this.outGoingGoods = outGoingGoods;
	}

	public int getFacilityIdDeliverTo() {
		return facilityIdDeliverTo;
	}

	public void setFacilityIdDeliverTo(int facilityIdDeliverTo) {
		this.facilityIdDeliverTo = facilityIdDeliverTo;
	}

	public int getOpenOrderAmount() {
		return openOrderAmount;
	}

	public void setOpenOrderAmount(int openOrderAmount) {
		this.openOrderAmount = openOrderAmount;
	}

	public int getOutGoingGoods() {
		return outGoingGoods;
	}

	public void setOutGoingGoods(int outGoingGoods) {
		this.outGoingGoods = outGoingGoods;
	}

	public int getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(int facilityId) {
		this.facilityId = facilityId;
	}
}
