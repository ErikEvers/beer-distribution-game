package org.han.ica.asd.c.fxml_helper.treebuilder;

import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import org.han.ica.asd.c.model.domain_objects.Facility;

/**
 * Class that contains display options for a facility in a distribution chain view.
 * @author Rick Zweers
 * @author Yarno Boelens
 */
public class FacilityRectangle extends StackPane {
    private Facility facility;
    private Rectangle rectangle;

	/**
	 * @param facility the particular object that this rectangle represents.
	 * @param assignedPlayer the name of the player controlling this facility.
	 * @param assignedAgent the name of the agent controlling this facility.
	 * @author Rick Zweers
	 * @author Yarno Boelens
	 */
	public FacilityRectangle(Facility facility, String assignedPlayer, String assignedAgent){
    		super();

        this.facility = facility;
        this.setCursor(Cursor.HAND);

				Text text = new Text("Player: " +assignedPlayer + "\n" + "Agent: " +assignedAgent);

				rectangle = new Rectangle(text.getLayoutBounds().getWidth() + 10, text.getLayoutBounds().getHeight() + 10, Color.web(determineColor(facility.getFacilityType().getFacilityName())));
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeType(StrokeType.INSIDE);
        rectangle.setArcHeight(5);
        rectangle.setArcWidth(5);

				this.getChildren().addAll(rectangle, text);

        this.setTranslateX(rectangle.getTranslateX());
        this.setTranslateY(rectangle.getTranslateY());
        this.setHeight(rectangle.getHeight());
        this.setWidth(rectangle.getWidth());
    }

	/**
	 * Returns a color based on the particular type of facility.
	 * @param facilityType the name of the type of facility.
	 * @return String color that matches the facility.
	 * @author Rick Zweers
	 */
	private static String determineColor(String facilityType) {
        String color = "#FFFFFF";

        if ("Factory".equalsIgnoreCase(facilityType)) {
            color = "#1fff34";

        } else if ("Regional Warehouse".equalsIgnoreCase(facilityType)) {
            color = "DODGERBLUE";

        } else if ("Wholesaler".equalsIgnoreCase(facilityType)) {
            color = "#ffbc1f";

        } else if ("Retailer".equalsIgnoreCase(facilityType)) {
            color = "#ff2151";
        }

        return color;
    }

	/**
	 * Add color around rectangle
	 * @author Yarno Boelens
	 */
	public void addShadow() {
		DropShadow e = new DropShadow();
		e.setWidth(20);
		e.setHeight(20);
		e.setRadius(25);
		e.setColor(Color.web("ff8000"));
		rectangle.setEffect(e);
	}

	/**
	 * Retrieve the facility
	 * @return Facility object
	 * @author Rick Zweers
	 */
	public Facility getFacility() {
        return facility;
    }

}
