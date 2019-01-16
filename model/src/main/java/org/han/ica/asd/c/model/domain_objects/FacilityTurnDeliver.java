package org.han.ica.asd.c.model.domain_objects;

import java.io.Serializable;

public class FacilityTurnDeliver implements Serializable, IFacility {
	private int facilityId;
	private int facilityIdDeliverTo;
	private int openOrderAmount;
	private int deliverAmount;

	public FacilityTurnDeliver() {

	}

	public FacilityTurnDeliver(int facilityId, int facilityIdDeliverTo, int openOrderAmount, int deliverAmount) {
		this.facilityId = facilityId;
		this.facilityIdDeliverTo = facilityIdDeliverTo;
		this.openOrderAmount = openOrderAmount;
		this.deliverAmount = deliverAmount;
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

	public int getDeliverAmount() {
		return deliverAmount;
	}

	public void setDeliverAmount(int deliverAmount) {
		this.deliverAmount = deliverAmount;
	}

	public int getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(int facilityId) {
		this.facilityId = facilityId;
	}
}
