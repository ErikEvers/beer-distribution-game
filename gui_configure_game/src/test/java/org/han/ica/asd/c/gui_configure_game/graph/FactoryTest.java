package org.han.ica.asd.c.gui_configure_game.graph;

import org.junit.Test;

import static org.mockito.Mockito.mock;

public class FactoryTest {

    @Test
    public void addBuyerToList() {
        //Arrange
        Factory factory = new Factory();
        RegionalWarehouse regionalWarehouse = mock(RegionalWarehouse.class);

        //Act
        factory.addBuyer(regionalWarehouse);

        //Assert
        assert(factory.buyers.contains(regionalWarehouse));
    }

    @Test
    public void removeBuyerFromList() {
        //Arrange
        Factory factory = new Factory();
        RegionalWarehouse regionalWarehouse = mock(RegionalWarehouse.class);

        factory.buyers.add(regionalWarehouse);

        //Act
        factory.removeBuyer(regionalWarehouse);

        //Assert
        assert(factory.buyers.isEmpty());
    }
}
