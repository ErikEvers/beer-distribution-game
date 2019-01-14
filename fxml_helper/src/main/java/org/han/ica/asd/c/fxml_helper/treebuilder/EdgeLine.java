package org.han.ica.asd.c.fxml_helper.treebuilder;

import javafx.scene.shape.Line;

/**
 * Line from one FacilityRectangle to the other.
 * @author Rick Zweers
 */
public class EdgeLine extends Line {

    /**
     * Draw the actual line.
     * @param r1 starting rectangle element
     * @param r2 ending rectangle element
     * @param x starting x coordinate
     * @param y starting y coordinate
     * @author Rick Zweers
     */
    public void drawLine(FacilityRectangle r1, FacilityRectangle r2, double x, double y){
        super.setStartX(x + (r1.getWidth()/2));
        super.setStartY(y + r1.getHeight());
        super.setEndX((r2.getTranslateX() + (r2.getWidth()/2)));
        super.setEndY(r2.getTranslateY());
    }
}
