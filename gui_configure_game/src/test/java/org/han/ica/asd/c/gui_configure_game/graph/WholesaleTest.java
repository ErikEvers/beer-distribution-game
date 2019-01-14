package org.han.ica.asd.c.gui_configure_game.graph;

import org.junit.Test;

import static org.mockito.Mockito.mock;

public class WholesaleTest {

    @Test
    public void addBuyerToList() {
        //Arrange
        Wholesale wholesale = new Wholesale();
        Retailer retailer = mock(Retailer.class);

        //Act
        wholesale.addBuyer(retailer);

        //Assert
        assert(wholesale.buyers.contains(retailer));
    }

    @Test
    public void removeBuyerFromList() {
        //Arrange
        Wholesale wholesale = new Wholesale();
        Retailer retailer = mock(Retailer.class);

        wholesale.buyers.add(retailer);

        //Act
        wholesale.removeBuyer(retailer);

        //Assert
        assert(wholesale.buyers.isEmpty());
    }

    @Test
    public void addSupplierToList() {
        //Arrange
        Wholesale wholesale = new Wholesale();
        RegionalWarehouse regionalWarehouse = mock(RegionalWarehouse.class);

        //Act
        wholesale.addSupplier(regionalWarehouse);

        //Assert
        assert(wholesale.suppliers.contains(regionalWarehouse));
    }

    @Test
    public void removeSupplierFromList() {
        //Arrange
        Wholesale wholesale = new Wholesale();
        RegionalWarehouse regionalWarehouse = mock(RegionalWarehouse.class);

        wholesale.suppliers.add(regionalWarehouse);

        //Act
        wholesale.removeSupplier(regionalWarehouse);

        //Assert
        assert(wholesale.suppliers.isEmpty());
    }
}
