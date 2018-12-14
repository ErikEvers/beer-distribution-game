package org.han.ica.asd.c.gui_play_game.see_other_facilities;

import javafx.scene.shape.Line;

public class EdgeLine extends Line {
    private FacilityRectangle buyer;
    private FacilityRectangle supplier;

    public FacilityRectangle getBuyer() {
        return buyer;
    }

    public void setBuyer(FacilityRectangle buyer) {
        this.buyer = buyer;
    }

    public FacilityRectangle getSupplier() {
        return supplier;
    }

    public void setSupplier(FacilityRectangle supplier) {
        this.supplier = supplier;
    }

    public void drawLine(FacilityRectangle r1, FacilityRectangle r2, double x, double y){
        super.setStartX(x + (r1.getWidth()/2));
        super.setStartY(y + r1.getHeight());
        super.setEndX((r2.getTranslateX() + (r2.getWidth()/2)));
        super.setEndY(r2.getTranslateY());
    }
}
