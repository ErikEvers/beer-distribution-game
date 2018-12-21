package org.han.ica.asd.c.model.domain_objects;

public class FacilityTurnDeliver {
	int facilityIdDeliverTo;
	int openOrderAmount;
	int outGoingGoods;

	public FacilityTurnDeliver(int facilityIdDeliverTo, int openOrderAmount, int outGoingGoods) {
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
}
