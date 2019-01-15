package org.han.ica.asd.c.model.interface_models;

public class ActionModel {
	public final String    	type;
	public int       		amount;
	public final int    	facilityId;

	/**
	 * Action model constructor
	 * @param type			Type of the action. Either order or deliver
	 * @param amount		The amount of goods ordered or delivered. Minimal value is 0.
	 * @param facilityId	The id of the facility that receives the order or delivery.
	 */
	public ActionModel(String type, int amount, int facilityId) {
		this.type = type;
		this.amount = Math.max(0, amount);
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
