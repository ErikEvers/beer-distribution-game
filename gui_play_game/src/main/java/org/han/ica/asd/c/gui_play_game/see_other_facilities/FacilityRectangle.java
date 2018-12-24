package org.han.ica.asd.c.gui_play_game.see_other_facilities;

import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import org.han.ica.asd.c.model.dao_model.FacilityDB;

public class FacilityRectangle extends Rectangle {
    private FacilityDB facility;

    public  FacilityRectangle(FacilityDB facility){
        super(51, 36, Color.web(determineColor(facility.getFacilityType())));
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

        } else if ("Regional warehouse".equals(facilityType)) {
            color = "DODGERBLUE";

        } else if ("Wholesale".equals(facilityType)) {
            color = "#ffbc1f";

        } else if ("Retailer".equals(facilityType)) {
            color = "#ff2151";
        }

        return color;
    }

    public FacilityDB getFacility() {
        return facility;
    }
}
