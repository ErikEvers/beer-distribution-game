package org.han.ica.asd.c.gui_configure_game.graph;

import org.junit.Test;

import static org.mockito.Mockito.mock;

public class RegionalWarehouseTest {

    @Test
    public void addBuyerToList() {
        //Arrange
        RegionalWarehouse regionalWarehouse = new RegionalWarehouse();
        Wholesale wholesale = mock(Wholesale.class);

        //Act
        regionalWarehouse.addBuyer(wholesale);

        //Assert
        assert(regionalWarehouse.buyers.contains(wholesale));
    }

    @Test
    public void removeBuyerFromList() {
        //Arrange
        RegionalWarehouse regionalWarehouse = new RegionalWarehouse();
        Wholesale wholesale = mock(Wholesale.class);

        regionalWarehouse.buyers.add(wholesale);

        //Act
        regionalWarehouse.removeBuyer(wholesale);

        //Assert
        assert(regionalWarehouse.buyers.isEmpty());
    }

    @Test
    public void addSupplierToList() {
        //Arrange
        RegionalWarehouse regionalWarehouse = new RegionalWarehouse();
        Factory factory = mock(Factory.class);

        //Act
        regionalWarehouse.addSupplier(factory);

        //Assert
        assert(regionalWarehouse.suppliers.contains(factory));
    }

    @Test
    public void removeSupplierFromList() {
        //Arrange
        RegionalWarehouse regionalWarehouse = new RegionalWarehouse();
        Factory factory = mock(Factory.class);

        regionalWarehouse.suppliers.add(factory);

        //Act
        regionalWarehouse.removeSupplier(factory);

        //Assert
        assert(regionalWarehouse.suppliers.isEmpty());
    }
}
