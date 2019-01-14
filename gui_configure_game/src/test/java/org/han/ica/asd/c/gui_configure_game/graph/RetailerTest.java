package org.han.ica.asd.c.gui_configure_game.graph;

import org.junit.Test;

import static org.mockito.Mockito.mock;

public class RetailerTest {

    @Test
    public void addSupplierToList() {
        //Arrange
        Retailer retailer = new Retailer();
        Wholesale wholesale = mock(Wholesale.class);

        //Act
        retailer.addSupplier(wholesale);

        //Assert
        assert(retailer.suppliers.contains(wholesale));
    }

    @Test
    public void removeSupplierFromList() {
        //Arrange
        Retailer retailer = new Retailer();
        Wholesale wholesale = mock(Wholesale.class);

        retailer.suppliers.add(wholesale);

        //Act
        retailer.removeSupplier(wholesale);

        //Assert
        assert(retailer.suppliers.isEmpty());
    }
}
