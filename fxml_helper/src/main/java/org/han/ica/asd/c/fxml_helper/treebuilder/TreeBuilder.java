package org.han.ica.asd.c.fxml_helper.treebuilder;

import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.GameAgent;
import org.han.ica.asd.c.model.domain_objects.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Class responsible for displaying the facility tree.
 * @author Rick Zweers
 * @author Yarno Boelens
 */
public class TreeBuilder {

	private List<FacilityRectangle> factories;
	private List<FacilityRectangle> wholesalers;
	private List<FacilityRectangle> warehouses;
	private List<FacilityRectangle> retailers;
	private List<FacilityRectangle> drawnFacilities;

	private boolean tooltipRequired;
	private AnchorPane container;
	private BeerGame beerGame;

	/**
	 * Method that loads facilities with its relevant edges.
	 *
	 * First it draws the facilities connected through the edge (if they haven't been drawn yet). Then it draws the corresponding edge between the facilities.
	 *
	 * @author Rick Zweers
	 * @author Yarno Boelens
	 */
	public void loadFacilityView(BeerGame beerGame, AnchorPane container, boolean tooltipRequired) {
		this.container = container;
		this.tooltipRequired = tooltipRequired;
		this.beerGame = beerGame;
		factories = new ArrayList<>();
		wholesalers = new ArrayList<>();
		warehouses = new ArrayList<>();
		retailers = new ArrayList<>();
		drawnFacilities = new ArrayList<>();

		container.getChildren().clear();

		Map<Facility, List<Facility>> links = beerGame.getConfiguration().getFacilitiesLinkedTo();

		for (Facility facility : links.keySet()) {
			for (Facility child : links.get(facility)) {
				drawLine(facility, child);
			}
		}
	}

	/**
	 * Draws the line between facilities in a facilitylinkedto on the screen.
	 *
	 * Facility rectangles visible on screen
	 * @param parent
	 * @param child
	 * Link/edge through which the facilities are connected
	 * @author Rick Zweers
	 * @author Yarno Boelens
	 */
	private void drawLine(Facility parent, Facility child) {
		EdgeLine line = new EdgeLine();
		Optional<FacilityRectangle> exisitingDeliver = drawnFacilities.stream().filter(facility -> facility.getFacility() == parent).findFirst();
		FacilityRectangle rectangleDeliver;
		if (exisitingDeliver.isPresent()) {
			rectangleDeliver = exisitingDeliver.get();
		} else {
			rectangleDeliver = drawFacility(parent);
			drawnFacilities.add(rectangleDeliver);
		}

		Optional<FacilityRectangle> exisitingOrder = drawnFacilities.stream().filter(facility -> facility.getFacility() == child).findFirst();
		FacilityRectangle rectangleOrder;
		if (exisitingOrder.isPresent()) {
			rectangleOrder = exisitingOrder.get();
		} else {
			rectangleOrder = drawFacility(child);
			drawnFacilities.add(rectangleOrder);
		}

		line.drawLine(rectangleOrder, rectangleDeliver, rectangleOrder.getTranslateX(), rectangleOrder.getTranslateY());
		setLineStroke(parent, child, line);

		container.getChildren().add(line);
	}

	/**
	 * Adjusts the colour based on the link being 'active'.
	 *
	 * @param parent
	 * @param child
	 * Link/edge through which the facilities are connected
	 * @param line
	 * Visual representation of the link.
	 * @author Rick Zweers
	 * @author Yarno Boelens
	 * TODO: add red stroke whenever either facility is bankrupt
	 */
	private static void setLineStroke(Facility parent, Facility child, EdgeLine line) {
		line.setStroke(Color.BLACK);
	}

	/**
	 * Calls the drawFacilityOnScreen method in different ways depending on the type of the given facility.
	 *
	 * @param facility Facility to be drawn.
	 * @return Returns facility that was drawn.
	 * @author Rick Zweers
	 * @author Yarno Boelens
	 */
	private FacilityRectangle drawFacility(Facility facility) {
		if("Factory".equalsIgnoreCase(facility.getFacilityType().getFacilityName())) {
			drawFacilityOnScreen(facility, factories, 0);
			return factories.get(factories.size()-1);
		} else if("Regional Warehouse".equalsIgnoreCase(facility.getFacilityType().getFacilityName())) {
			drawFacilityOnScreen(facility, warehouses, 1);
			return warehouses.get(warehouses.size()-1);
		} else if("Wholesaler".equalsIgnoreCase(facility.getFacilityType().getFacilityName())) {
			drawFacilityOnScreen(facility, wholesalers, 2);
			return wholesalers.get(wholesalers.size()-1);
		} else {
			drawFacilityOnScreen(facility, retailers, 3);
			return retailers.get(retailers.size() - 1);
		}
	}

	/**
	 * Draws the facility on the screen.
	 * @param facility Facility to be drawn.
	 * @param facilityList List of drawn facility rectangles.
	 * @param y Y-axis on which the facility is to be drawn.
	 * @author Rick Zweers
	 * @author Yarno Boelens
	 */
	private void drawFacilityOnScreen(Facility facility, List<FacilityRectangle> facilityList, int y) {
		double rows = (container.getPrefHeight()/4);
		double columns = 60;
		container.getChildren().removeAll(facilityList);
		facilityList.add(createRectangle(facility));
		for (int i = 0; i < facilityList.size(); i++) {
			FacilityRectangle node = facilityList.get(i);
			node.setTranslateX(columns * i);
			node.setTranslateY(rows * y);
			if ((node.getTranslateX() + node.getWidth()) > container.getMinWidth()){
				container.setMinWidth(node.getTranslateX() + node.getWidth());
			}
			container.getChildren().add(node);
		}
	}

	/**
	 * Method creates a new FacilityRectangle based on the facility given to the method.
	 * @return New FacilityRectangle.
	 * @author Rick Zweers
	 * @author Yarno Boelens
	 */
	private FacilityRectangle createRectangle(Facility facility) {
		FacilityRectangle rectangle = new FacilityRectangle(facility, getPlayerName(beerGame.getPlayers(), beerGame.getAgents(), facility));
		if(tooltipRequired) {
			installTooltip(facility, rectangle);
		}
		return rectangle;
	}

	/**
	 * Installs the tooltip with information regarding the facility on the rectangle.
	 * @author Rick Zweers
	 * @author Yarno Boelens
	 */
	private void installTooltip(Facility facility, FacilityRectangle rectangle) {
		String tooltipString = facility.getFacilityType().getFacilityName() + " - " + facility.getFacilityId() +
				" overview turn x\nBacklog: 25\nInventory: 0\nMoney: 500";
		Tooltip tooltip = new Tooltip(tooltipString);
		Tooltip.install(rectangle, tooltip);
	}

	/**
	 * Fetch the name of the player or agent that is controlling the facility.
	 * @param players list of all players.
	 * @param agents list of all agents.
	 * @param facility the facility to check.
	 * @return name of the controlling entity.
	 * @author Yarno Boelens
	 */
	private String getPlayerName(List<Player> players, List<GameAgent> agents, Facility facility) {
		for(Player player: players) {
			if(player.getFacility() == facility) {
				return player.getName();
			}
		}
		for(GameAgent agent: agents) {
			if(agent.getFacility() == facility) {
				return agent.getGameAgentName();
			}
		}
		return "";
	}
}
