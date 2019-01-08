package org.han.ica.asd.c.fxml_helper.treebuilder;

import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class responsible for displaying the facility tree.
 * Most of the code is created by Rick Zweers.
 */
public class TreeBuilder {

	private static ArrayList<FacilityRectangle> factories;
	private static ArrayList<FacilityRectangle> wholesalers;
	private static ArrayList<FacilityRectangle> warehouses;
	private static ArrayList<FacilityRectangle> retailers;
	private static boolean tooltipRequired;
	private static AnchorPane container;

	/**
	 * Method that loads facilities with its relevant edges.
	 *
	 * First it draws the facilities connected through the edge (if they haven't been drawn yet). Then it draws the corresponding edge between the facilities.
	 *
	 * When a facility is of an unknown type.
	 */
	public static void loadFacilityView(Map<Facility, List<Facility>> links, AnchorPane container, boolean tooltipRequired) {
		TreeBuilder.container = container;
		TreeBuilder.tooltipRequired = tooltipRequired;
		TreeBuilder.factories = new ArrayList<>();
		TreeBuilder.wholesalers = new ArrayList<>();
		TreeBuilder.warehouses = new ArrayList<>();
		TreeBuilder.retailers = new ArrayList<>();

		ArrayList<Facility> drawnFacilities = new ArrayList<>();
		ArrayList<FacilityRectangle> drawnFacilityRectangles = new ArrayList<>();

		for (Facility facility : links.keySet()) {
			for (Facility child : links.get(facility)) {
				drawFacilities(drawnFacilities, drawnFacilityRectangles, facility, child);
				drawLine(container, drawnFacilityRectangles, facility, child);
			}
		}
	}

	/**
	 * Draws the facilities in the link on the screen.
	 *
	 * @param drawnFacilities
	 * Facility objects represented on screen
	 * @param drawnFacilityRectangles
	 * Facility rectangles visible on screen
	 * @param parent
	 * Link/edge through which the facilities are connected
	 */
	private static void drawFacilities(ArrayList<Facility> drawnFacilities,
															ArrayList<FacilityRectangle> drawnFacilityRectangles,
															Facility parent, Facility child) {
		if(!drawnFacilities.contains(parent)) {
			drawnFacilityRectangles.add(drawFacility(parent));
			drawnFacilities.add(parent);
		}

		if(!drawnFacilities.contains(child)) {
			drawnFacilityRectangles.add(drawFacility(child));
			drawnFacilities.add(child);
		}
	}

	/**
	 * Draws the line between facilities in a facilitylinkedto on the screen.
	 *
	 * @param drawnFacilityRectangles
	 * Facility rectangles visible on screen
	 * @param parent
	 * @param child
	 * Link/edge through which the facilities are connected
	 */
	private static void drawLine(AnchorPane container, ArrayList<FacilityRectangle> drawnFacilityRectangles, Facility parent, Facility child) {
		EdgeLine line = new EdgeLine();
		FacilityRectangle rectangleDeliver = new FacilityRectangle(new Facility(new FacilityType("Retailer", 0, 0, 0, 0, 0, 0, 0), 1));
		FacilityRectangle rectangleOrder = new FacilityRectangle(new Facility(new FacilityType("Retailer", 0, 0, 0, 0, 0, 0, 0), 1));

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
	 */
	private static void setLineStroke(Facility parent, Facility child, EdgeLine line) {
		line.setStroke(Color.BLACK);
	}

	/**
	 * Calls the drawFacilityOnScreen method in different ways depending on the type of the given facility.
	 *
	 * @param facility
	 * Facility to be drawn.
	 * @return
	 * Returns facility that was drawn.
	 */
	private static FacilityRectangle drawFacility(Facility facility) {
		if("Factory".equalsIgnoreCase(facility.getFacilityType().getFacilityName())) {
			drawFacilityOnScreen(facility, factories, 0);
			return factories.get(factories.size()-1);
		} else if("Regional Warehouse".equalsIgnoreCase(facility.getFacilityType().getFacilityName())) {
			drawFacilityOnScreen(facility, warehouses, 1);
			return warehouses.get(warehouses.size()-1);
		} else if("Wholesale".equalsIgnoreCase(facility.getFacilityType().getFacilityName())) {
			drawFacilityOnScreen(facility, wholesalers, 2);
			return wholesalers.get(wholesalers.size()-1);
		} else {
			drawFacilityOnScreen(facility, retailers, 3);
			return retailers.get(retailers.size() - 1);
		}
	}

	/**
	 * Draws the facility on the screen.
	 * @param facility
	 * Facility to be drawn.
	 * @param facilityList
	 * List of drawn facility rectangles.
	 * @param y
	 * Y-axis on which the facility is to be drawn.
	 */
	private static void drawFacilityOnScreen(Facility facility, ArrayList<FacilityRectangle> facilityList, int y) {
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
	 */
	private static FacilityRectangle createRectangle(Facility facility) {
		FacilityRectangle rectangle = new FacilityRectangle(facility);
		if(tooltipRequired) {
			installTooltip(facility, rectangle);
		}
		return rectangle;
	}

	/**
	 * Installs the tooltip with information regarding the facility on the rectangle.
	 */
	private static void installTooltip(Facility facility, FacilityRectangle rectangle) {
		String tooltipString = facility.getFacilityType().getFacilityName() + " - " + facility.getFacilityId() +
				" overview turn x\nBacklog: 25\nInventory: 0\nMoney: 500";
		Tooltip tooltip = new Tooltip(tooltipString);
		Tooltip.install(rectangle, tooltip);
	}
}
