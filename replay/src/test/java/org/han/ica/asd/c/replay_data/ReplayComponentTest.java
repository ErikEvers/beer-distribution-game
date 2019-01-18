package org.han.ica.asd.c.replay_data;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import org.han.ica.asd.c.gamevalue.GameValue;
import org.han.ica.asd.c.interfaces.gui_replay_game.IVisualisedPlayedGameData;
import org.han.ica.asd.c.interfaces.replay.IRetrieveReplayData;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReplayComponentTest {

    private IVisualisedPlayedGameData replayComponent;

    @BeforeEach
    public void setup() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IRetrieveReplayData.class).to(RetrieveReplayDataStub.class);
            }
        });
        replayComponent = injector.getInstance(ReplayComponent.class);
    }

    @Test
    public void getRoundZeroFactoryBudget() {
        replayComponent.setDisplayedAttribute(GameValue.BUDGET);
        replayComponent.addDisplayedFacility(GameValue.FACTORY);

        ObservableList<XYChart.Series<Double, Double>> list = replayComponent.getChartData();
        Assert.assertEquals(0, list.get(0).getData().get(0).getXValue().intValue());
        Assert.assertEquals(10, list.get(0).getData().get(0).getYValue().intValue());
    }

    @Test
    public void getRoundZeroWholesalerStock() {
        replayComponent.setDisplayedAttribute(GameValue.STOCK);
        replayComponent.addDisplayedFacility(GameValue.WHOLESALER);

        ObservableList<XYChart.Series<Double, Double>> list = replayComponent.getChartData();
        Assert.assertEquals(0, list.get(0).getData().get(0).getXValue().intValue());
        Assert.assertEquals(40, list.get(0).getData().get(0).getYValue().intValue());
    }

    @Test
    public void getRoundZeroRetailerBacklog() {
        replayComponent.setDisplayedAttribute(GameValue.BACKLOG);
        replayComponent.addDisplayedFacility(GameValue.RETAILER);

        ObservableList<XYChart.Series<Double, Double>> list = replayComponent.getChartData();
        Assert.assertEquals(0, list.get(0).getData().get(0).getXValue().intValue());
        Assert.assertEquals(200, list.get(0).getData().get(0).getYValue().intValue());
    }

    @Test
    public void getRoundZeroWarehouseOrdered() {
        replayComponent.setDisplayedAttribute(GameValue.ORDERED);
        replayComponent.addDisplayedFacility(GameValue.REGIONALWAREHOUSE);

        ObservableList<XYChart.Series<Double, Double>> list = replayComponent.getChartData();
        Assert.assertEquals(0, list.get(0).getData().get(0).getXValue().intValue());
        Assert.assertEquals(5, list.get(0).getData().get(0).getYValue().intValue());
    }

    @Test
    public void getLastRoundFactoryOutgoingGoods() {
        replayComponent.setDisplayedAttribute(GameValue.OUTGOINGGOODS);
        replayComponent.addDisplayedFacility(GameValue.FACTORY);

        replayComponent.updateCurrentRound(10);

        ObservableList<XYChart.Series<Double, Double>> list = replayComponent.getChartData();
        Assert.assertEquals(10, list.get(0).getData().get(10).getXValue().intValue());
        Assert.assertEquals(2000, list.get(0).getData().get(10).getYValue().intValue());
    }

    @Test
    public void getAllFacilities() {
        Assert.assertEquals(9, replayComponent.getAllFacilities().size());
    }

    @Test
    public void getTotalRounds() {
        Assert.assertEquals("10", replayComponent.getTotalRoundsString());
    }

    @Test
    public void removeAverageFromDisplayed() {
        replayComponent.setDisplayedAttribute(GameValue.BUDGET);
        replayComponent.addDisplayedFacility(GameValue.FACTORY);

        ObservableList<XYChart.Series<Double, Double>> list = replayComponent.getChartData();
        Assert.assertEquals(0, list.get(0).getData().get(0).getXValue().intValue());
        Assert.assertEquals(10, list.get(0).getData().get(0).getYValue().intValue());

        replayComponent.removeDisplayedFacility(GameValue.FACTORY);

        list = replayComponent.getChartData();
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void incrementCurrentRound() {
        Assert.assertTrue(replayComponent.incrementCurrentRound());
        Assert.assertEquals("2", replayComponent.getCurrentRoundString());

        replayComponent.updateCurrentRound(10);
        Assert.assertFalse(replayComponent.incrementCurrentRound());
    }

    @Test
    public void decrementCurrentRound() {
        replayComponent.updateCurrentRound(2);
        Assert.assertTrue(replayComponent.decrementCurrentRound());
        Assert.assertEquals("1", replayComponent.getCurrentRoundString());

        Assert.assertFalse(replayComponent.decrementCurrentRound());
    }
}
