package org.han.ica.asd.c.model.domain_objects;

import java.util.Collections;
import java.util.Map;

public class GameRoundAction {
	public final Map<Facility, Integer> targetOrderMap;
	public final Map<Facility, Integer> targetDeliverMap;

	/**
	 * Constructor with default targetOrderMap and targetDeliverMap
	 *
	 * @param targetOrderMap    A map with the orders that an agent wants to do
	 * @param targetDeliverMap  A map wih the delivers that an agent wants to do
	 */
	public GameRoundAction(Map<Facility, Integer> targetOrderMap, Map<Facility, Integer> targetDeliverMap) {
		this.targetOrderMap = Collections.unmodifiableMap(targetOrderMap);
		this.targetDeliverMap = Collections.unmodifiableMap(targetDeliverMap);
	}
}
