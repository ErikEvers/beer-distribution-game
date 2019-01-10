package org.han.ica.asd.c.gui_configure_game.graph;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class GraphTest {

    @Test
    public void addFacilityToList() {
        //Arrange
        Graph graph = new Graph();
        Factory factory = Mockito.mock(Factory.class);

        //Act
        graph.addFacility(factory);

        //Assert
        assert(graph.getFacilities().contains(factory));
    }

    @Test
    public void removeFacilityRemovesFromList() {
        //Arrange
        Graph graph = new Graph();
        Factory factory = Mockito.mock(Factory.class);

        graph.facilities.add(factory);

        //Act
        graph.removeFacility(factory);

        //Assert
        assert(graph.getFacilities().isEmpty());
    }

    @Test
    public void removeFacilityRemovesRelevantEdges() {
        //Arrange
        Graph graph = new Graph();
        Factory factory = new Factory();
        RegionalWarehouse regionalWarehouse = new RegionalWarehouse();
        Wholesale wholesale = new Wholesale();

        graph.facilities.add(factory);
        graph.facilities.add(regionalWarehouse);
        graph.facilities.add(wholesale);

        factory.buyers.add(regionalWarehouse);
        regionalWarehouse.suppliers.add(factory);

        regionalWarehouse.buyers.add(wholesale);
        wholesale.suppliers.add(regionalWarehouse);

        //Act
        graph.removeFacility(regionalWarehouse);

        //Assert
        assert(factory.buyers.isEmpty());
        assert(wholesale.suppliers.isEmpty());
    }

    @Test
    public void removeEdgeRemovesBuyerFromList() {
        //Arrange
        Graph graph = new Graph();
        Factory factory = new Factory();
        RegionalWarehouse regionalWarehouse = new RegionalWarehouse();

        factory.buyers.add(regionalWarehouse);
        regionalWarehouse.suppliers.add(factory);

        //Act
        graph.removeEdge(factory, regionalWarehouse);

        //Assert
        assert(factory.getBuyers().isEmpty());
    }

    @Test
    public void removeEdgeRemovesSupplierFromList() {
        //Arrange
        Graph graph = new Graph();
        Factory factory = new Factory();
        RegionalWarehouse regionalWarehouse = new RegionalWarehouse();

        factory.buyers.add(regionalWarehouse);
        regionalWarehouse.suppliers.add(factory);

        //Act
        graph.removeEdge(factory, regionalWarehouse);

        //Assert
        assert(regionalWarehouse.getSuppliers().isEmpty());
    }


    @Test(expected = GraphException.class)
    public void impossibleToCreateEdgeBetweenFacilitiesNotInList() throws GraphException {
        //Arrange
        Graph graph = new Graph();
        Factory factory = new Factory();
        RegionalWarehouse regionalWarehouse = new RegionalWarehouse();

        //Act
        graph.addEdge(factory, regionalWarehouse);
    }
}
