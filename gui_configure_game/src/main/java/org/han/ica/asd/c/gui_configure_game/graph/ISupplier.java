package org.han.ica.asd.c.gui_configure_game.graph;

public interface ISupplier<T> {
    void addBuyer(T buyer);
    void removeBuyer(T buyer);
}
