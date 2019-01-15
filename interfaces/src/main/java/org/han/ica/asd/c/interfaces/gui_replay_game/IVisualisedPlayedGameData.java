package org.han.ica.asd.c.interfaces.gui_replay_game;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import org.han.ica.asd.c.gamevalue.GameValue;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;

import java.util.List;

public interface IVisualisedPlayedGameData {
    /***
     *
     * @return the total rounds converted to String
     */
    String getTotalRoundsString();

    /***
     *
     * @return the current round converted to String
     */
    String getCurrentRoundString();

    /***
     * Increments the currentRound if the currentRound is lower than the total rounds
     * @return true if the currentRound was incremented, false if unchanged
     */
    boolean incrementCurrentRound();

    /***
     * Decrements the currentRound if the currentRound is higher than the LOWEST_ROUND_POSSIBLE
     * @return true if the currentRound was decremented, false if unchanged
     */
    boolean decrementCurrentRound();

    /***
     * Updates the currentRound
     * @param round
     * The round to which currentRound will be updated
     */
    void updateCurrentRound(int round);

    /***
     *
     * @return A list with all Facilities available in the current BeerGame
     */
    List<Facility> getAllFacilities();

    /***
     * Generates the Graph data according to the selected facilities
     * @return A list with the Graph data in an Observable list
     */
    ObservableList<XYChart.Series<Double, Double>> getChartData();

    /***
     * Removes a Facility from the DisplayedAverages list
     * @param facility
     * The gamevalue of the Facility that is being removed
     */
    void removeDisplayedFacility(GameValue facility);

    /***
     * Adds a Facility to the DisplayedAverages list
     * @param facility
     * The gamevalue of the Facility that is being added
     */
    void addDisplayedFacility(GameValue facility);

    /***
     * Adds a Facility to the DisplayedFacilities list
     * @param facility
     * The Facility that is being added
     */
    void addDisplayedFacility(Facility facility);

    /***
     * Removes a Facility from the DisplayedFacilities list
     * @param facility
     */
    void removeDisplayedFacility(Facility facility);

    /***
     * Updates the Attribute that is being displayed
     * @param value
     * The attribute that needs to be displayed
     */
    void setDisplayedAttribute(GameValue value);

    /***
     * Get an entire beerGame with rounds filtered to the current round
     * @return
     * The beerGame filtered on currentRound
     */
    BeerGame getBeerGameForCurrentRound();

    /***
     * Get the Name of the current BeerGame
     * @return
     *
     */
    String getBeerGameName();
}
