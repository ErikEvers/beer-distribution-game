package org.han.ica.asd.c.gui_configure_game.graph;

import org.han.ica.asd.c.gui_configure_game.graph.EdgeLine;
import org.han.ica.asd.c.gui_configure_game.graph.GraphFacility;
import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class FacilityRectangle extends Rectangle {

    @Inject
    private GraphFacility graphFacility;

    private ArrayList<EdgeLine> lines;


    public  FacilityRectangle(GraphFacility graphFacility){
        super(51, 36, Color.web(graphFacility.getColor()));
        this.graphFacility = graphFacility;
        super.setStroke(Color.BLACK);
        super.setStrokeType(StrokeType.INSIDE);
        super.setArcHeight(5);
        super.setArcWidth(5);
        super.setCursor(Cursor.HAND);
        this.lines = new ArrayList<EdgeLine>();
    }

    public GraphFacility getGraphFacility() {
        return graphFacility;
    }

    public void setGraphFacility(GraphFacility graphFacility){
        this.graphFacility = graphFacility;
    }

    public List<EdgeLine> getLines() {
        return lines;
    }
}
