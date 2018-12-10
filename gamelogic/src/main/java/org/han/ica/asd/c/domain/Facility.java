package org.han.ica.asd.c.domain;

public class Facility {
    private int id;
    private String type;

    public Facility(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
