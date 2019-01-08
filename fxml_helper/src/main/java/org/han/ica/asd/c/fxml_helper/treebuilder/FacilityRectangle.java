package org.han.ica.asd.c.fxml_helper.treebuilder;

import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import org.han.ica.asd.c.model.domain_objects.Facility;

public class FacilityRectangle extends Rectangle {
    private Facility facility;

    public  FacilityRectangle(Facility facility){
        super(51, 36, Color.web(determineColor(facility.getFacilityType().getFacilityName())));
        this.facility = facility;
        super.setStroke(Color.BLACK);
        super.setStrokeType(StrokeType.INSIDE);
        super.setArcHeight(5);
        super.setArcWidth(5);
        super.setCursor(Cursor.HAND);
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
