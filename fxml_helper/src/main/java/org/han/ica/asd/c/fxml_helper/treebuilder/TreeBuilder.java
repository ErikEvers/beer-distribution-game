package org.han.ica.asd.c.fxml_helper.treebuilder;

import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityType;
import org.han.ica.asd.c.model.domain_objects.GameAgent;
import org.han.ica.asd.c.model.domain_objects.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class responsible for displaying the facility tree.
 * @author Rick Zweers
 * @author Yarno Boelens
 */
public class TreeBuilder {

	private static List<FacilityRectangle> factories;
	private static List<FacilityRectangle> wholesalers;
	private static List<FacilityRectangle> warehouses;
	private static List<FacilityRectangle> retailers;

	private static List<Facility> drawnFacilities;
	private static List<FacilityRectangle> drawnFacilityRectangles;

	private static boolean tooltipRequired;
	private static AnchorPane container;

	private static BeerGame beerGame;

	/**
	 * Method that loads facilities with its relevant edges.
	 *
	 * First it draws the facilities connected through the edge (if they haven't been drawn yet). Then it draws the corresponding edge between the facilities.
	 *
	 * @author Rick Zweers
	 * @author Yarno Boelens
	 */
	public static void loadFacilityView(BeerGame beerGame, AnchorPane container, boolean tooltipRequired) {
		TreeBuilder.container = container;
		TreeBuilder.tooltipRequired = tooltipRequired;
		TreeBuilder.beerGame = beerGame;
		factories = new ArrayList<>();
		wholesalers = new ArrayList<>();
		warehouses = new ArrayList<>();
		retailers = new ArrayList<>();
		drawnFacilities = new ArrayList<>();
		drawnFacilityRectangles = new ArrayList<>();

		Map<Facility, List<Facility>> links = beerGame.getConfiguration().getFacilitiesLinkedTo();

		for (Facility facility : links.keySet()) {
			addFacility(facility);
			for (Facility child : links.get(facility)) {
				addFacility(child);
				drawLine(facility, child);
			}
		}
	}

	/**
	 * Draws the facility in the link on the screen.
	 * @author Rick Zweers
	 * @author Yarno Boelens
	 */
	private static void addFacility(Facility facility) {
		if(!drawnFacilities.contains(facility)) {
			drawnFacilityRectangles.add(drawFacility(facility));
			drawnFacilities.add(facility);
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
	private static void drawLine(Facility parent, Facility child) {
		EdgeLine line = new EdgeLine();
		FacilityRectangle rectangleDeliver = new FacilityRectangle(new Facility(new FacilityType("Retailer", 0, 0, 0, 0, 0, 0, 0), 1),"");
		FacilityRectangle rectangleOrder = new FacilityRectangle(new Facility(new FacilityType("Retailer", 0, 0, 0, 0, 0, 0, 0), 1),"");

		for(FacilityRectangle rectangle : drawnFacilityRectangles) {

			if(rectangle.getFacility() == child) {
				rectangleDeliver = rectangle;
			}
			if(rectangle.getFacility() == parent) {
				rectangleOrder = rectangle;
			}
		}

		line.drawLine(rectangleDeliver, rectangleOrder, rectangleDeliver.getTranslateX(), rectangleDeliver.getTranslateY());
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
	private static FacilityRectangle drawFacility(Facility facility) {
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
	private static void drawFacilityOnScreen(Facility facility, List<FacilityRectangle> facilityList, int y) {
		double rows = (container.getPrefHeight()/4);
		double columns = 60;
		container.getChildren().removeAll(facilityList);
		facilityList.add(createRectangle(facility));
		for (int i = 0; i < facilityList.size(); i++) {
			facilityList.get(i).setTranslateX(columns * i);
			facilityList.get(i).setTranslateY(rows * y);
			if ((facilityList.get(i).getTranslateX() + facilityList.get(i).getWidth()) > container.getMinWidth()){
				container.setMinWidth(facilityList.get(i).getTranslateX() + facilityList.get(i).getWidth());
			}
			container.getChildren().add(facilityList.get(i));
		}
	}

	/**
	 * Method creates a new FacilityRectangle based on the facility given to the method.
	 * @return New FacilityRectangle.
	 * @author Rick Zweers
	 * @author Yarno Boelens
	 */
	private static FacilityRectangle createRectangle(Facility facility) {
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
	private static void installTooltip(Facility facility, FacilityRectangle rectangle) {
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
	private static String getPlayerName(List<Player> players, List<GameAgent> agents, Facility facility) {
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
