package org.han.ica.asd.c.fxml_helper.treebuilder;

import javafx.scene.Cursor;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import org.han.ica.asd.c.model.domain_objects.Facility;

public class FacilityRectangle extends StackPane {
    private Facility facility;

    public FacilityRectangle(Facility facility, String owner){
    		super();
        this.facility = facility;
				this.setCursor(Cursor.HAND);

				Rectangle rectangle = new Rectangle(51, 36, Color.web(determineColor(facility.getFacilityType().getFacilityName())));
				rectangle.setStroke(Color.BLACK);
				rectangle.setStrokeType(StrokeType.INSIDE);
				rectangle.setArcHeight(5);
				rectangle.setArcWidth(5);

				this.setTranslateX(rectangle.getTranslateX());
				this.setTranslateY(rectangle.getTranslateY());
				this.setHeight(rectangle.getHeight());
				this.setWidth(rectangle.getWidth());

				Text text = new Text(owner);
        this.getChildren().addAll(rectangle, text);
    }

    private static String determineColor(String facilityType) {
        String color = "#FFFFFF";

        if ("Factory".equals(facilityType)) {
            color = "#1fff34";

        } else if ("Regional Warehouse".equals(facilityType)) {
            color = "DODGERBLUE";

        } else if ("Wholesaler".equals(facilityType)) {
            color = "#ffbc1f";

        } else if ("Retailer".equals(facilityType)) {
            color = "#ff2151";
        }

        return color;
    }

    public Facility getFacility() {
        return facility;
    }
}
