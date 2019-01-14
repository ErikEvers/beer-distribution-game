package org.han.ica.asd.c.gui_configure_game.graph;

import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.util.ArrayList;
import java.util.List;

public class FacilityRectangle extends Rectangle {


    private GraphFacility graphFacility;

    private ArrayList<EdgeLine> lines;


    public FacilityRectangle(GraphFacility graphFacility) {
        super(51, 36, Color.web(graphFacility.getColor()));
        this.graphFacility = graphFacility;
        super.setStroke(Color.BLACK);
        super.setStrokeType(StrokeType.INSIDE);
        super.setArcHeight(5);
        super.setArcWidth(5);
        super.setCursor(Cursor.HAND);
        this.lines = new ArrayList<>();
    }

    public GraphFacility getGraphFacility() {
        return graphFacility;
    }

    public List<EdgeLine> getLines() {
        return lines;
    }
}
