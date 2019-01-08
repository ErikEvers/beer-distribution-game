package org.han.ica.asd.c.model.interface_models;

public class ActionModel {
	public final String    type;
	public final int       amount;
	public final int       facilityId;

	public ActionModel(String type, int amount, int facilityId) {
		this.type = type;
		this.amount = amount;
		this.facilityId = facilityId;
	}

	/**
	 * Determines if the action type is order.
	 *
	 * @return Returns a boolean which states if the Action type is order.
	 */
	public boolean isOrderType() {
		return "order".equals(type);
	}

	/**
	 * Determines if the action type is deliver.
	 *
	 * @return Returns a boolean which states if the Action type is deliver.
	 */
	public boolean isDeliverType() {
		return "deliver".equals(type);
	}
}
