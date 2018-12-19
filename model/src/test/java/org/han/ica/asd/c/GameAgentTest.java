package org.han.ica.asd.c;

import com.google.common.collect.Lists;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityLinkedTo;
import org.han.ica.asd.c.model.domain_objects.FacilityType;
import org.han.ica.asd.c.model.domain_objects.GameAgent;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class GameAgentTest {

	@Test
	public void test() {

		GameAgent gameAgent = new GameAgent("Game agent 1", new Facility(
				new FacilityType("Regional warehouse 1", 3, 3, 10, 30, 500, 6),
				Lists.newArrayList(new FacilityLinkedTo(
						"game 1",
						new Facility(
							new FacilityType("Factory 1", 3, 3, 10, 30, 500, 6),
							null,
							0),
						true
				), new FacilityLinkedTo(
						"game 1",
						new Facility(
								new FacilityType("Factory 2", 3, 3, 10, 30, 500, 6),
								null,
								0),
						true
				)),
				1));
		List<Map<Facility, Map<Facility, Integer>>> actionList = gameAgent.generateOrder();


	}
}
