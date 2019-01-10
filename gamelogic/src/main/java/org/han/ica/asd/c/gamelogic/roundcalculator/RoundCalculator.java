package org.han.ica.asd.c.gamelogic.roundcalculator;

import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityType;
import org.han.ica.asd.c.model.domain_objects.Round;

import java.util.List;
import java.util.Map;

public class RoundCalculator {
    private int currentTurnBackOrders;

    public RoundCalculator() {
        currentTurnBackOrders = 0; //Zero unless a backorder is calculated in calculateNewFacilityStockDeliver.
    }

    public Round calculateRound(Round previousRound, Round currentRound, Map<Facility, List<Facility>> facilityLinks) {
        for(Map.Entry<Facility, List<Facility>> entry : facilityLinks.entrySet()) {
            Facility order = entry.getKey();

            for(Facility deliver: entry.getValue()) {
                int ordered = previousRound.getTurnOrderByFacility(order, deliver);

                ordered = dealWithBackOrders(ordered, previousRound, order, deliver);

                ordered = calculateNewFacilityStockDeliver(currentRound, ordered, order, deliver);

                updateStock(currentRound, ordered, order, deliver);
            }
        }

        updateRemainingBudget(currentRound);
        return currentRound;
    }

    /**
     * Calculate and update the new stock of the facility that ordered.
     * Also add the delivered goods to the TurnDeliver list.
     * @param round
     * @param facilityOrder
     * @param facilityDeliver
     * @param ordered
     */
    private void updateStock(Round round, int ordered, Facility facilityOrder, Facility facilityDeliver) {
        int newFacilityStockOrder = round.getStockByFacility(facilityOrder) + ordered;
        calculateIncomingGoodsCosts(ordered, round, facilityOrder);

        round.updateStock(facilityOrder, newFacilityStockOrder);
        round.addTurnDeliver(facilityOrder, facilityDeliver, ordered, currentTurnBackOrders);
    }

    /**
     * calculates if a facility can deliver all the ordered goods. If not, the amount it can not deliver will be added as a backOrder and the amount it can deliver will be returned.
     * The stock of the facility that delivers will also be updated.
     * @param round
     * @param ordered
     * @param facilityDeliver
     * @param facilityOrder
     * @return
     */
    private int calculateNewFacilityStockDeliver(Round round, int ordered, Facility facilityDeliver, Facility facilityOrder) {
        int delivered = ordered;
        currentTurnBackOrders = 0;

        if (!facilityOrder.equals(facilityDeliver)) {
            int facilityStockDeliver = round.getStockByFacility(facilityDeliver);
            int newFacilityStockDeliver = facilityStockDeliver - ordered;

            //if the newFacilityStockDeliver is below 0, it means the facility that needs to deliver beer doesn't have enough beer in stock to deliver all, meaning he gets back orders.
            if (newFacilityStockDeliver < 0) {
                //determine how much beer can be delivered
                delivered = newFacilityStockDeliver + ordered;

                //backOrders is added as a positive value.
                currentTurnBackOrders = -newFacilityStockDeliver;

                //stock is 0 if you delivered all your beer
                newFacilityStockDeliver = 0;
            }

            calculateOutgoingGoodsEarnings(delivered, round, facilityDeliver);
            round.updateStock(facilityDeliver, newFacilityStockDeliver);
        }

        return delivered;
    }

    /**
     * Calculates the new remaining budget.
     * The remaining budget for facility Order gets calculated on basis of the stock.
     * The remaining budget for the facility Deliver gets calculated on basis of the backOrders it has.
     * @param round
     */
    private void updateRemainingBudget(Round round) {
        for (FacilityLinkedTo f : facilitityLinks) {
            Facility facilityOrder = f.getFacilityOrder();
            Facility facilityDeliver = f.getFacilityDeliver();
            if (round.isRemainingBudgetExisting(facilityOrder)) {
                //The budget is calculated for the FacilityOrder variable
                round.updateRemainingBudget(
                        calculateStockCost(round, facilityOrder),
                        facilityOrder);

                //The budget is calculated for the FacilityDeliver variable
                round.updateRemainingBudget(
                        calculateBackLogCost(round, facilityOrder, facilityDeliver),
                        facilityDeliver);
            }
        }
    }

    /**
     * Calculate the stock cost.
     * @param round
     * @param facilityOrder
     * @return
     */
    private int calculateStockCost(Round round, Facility facilityOrder) {
        int remainingBudget = round.getRemainingBudgetByFacility(facilityOrder);
        int stock = round.getStockByFacility(facilityOrder);
        FacilityType facilityType = facilityOrder.getFacilityType();
        int stockCosts = stock * facilityType.getStockHoldingCosts();
        return (remainingBudget - stockCosts);
    }

    /**
     * Because of the structure of our maps it's not possible to calculate the stock and the backlog cost for the same facility in the same function.
     * @param round
     * @param facilityOrder
     * @param facilityDeliver
     * @return
     */
    private int calculateBackLogCost(Round round, Facility facilityOrder, Facility facilityDeliver) {
        int remainingBudgetFacilityDeliver = round.getRemainingBudgetByFacility(facilityDeliver);
        if (round.isTurnBackLogFilledByFacility(facilityOrder)) {
            FacilityType facilityType = facilityDeliver.getFacilityType();
            int backOrders = round.getTurnBacklogByFacility(facilityOrder, facilityDeliver);
            int backlogCosts = backOrders * facilityType.getOpenOrderCosts();

            return (remainingBudgetFacilityDeliver - backlogCosts);
        }

        return remainingBudgetFacilityDeliver;
    }

    /**
     * Calculate the outgoing going goods value which is being earned by the facility deliver.
     * @param ordered
     * @param round
     * @param facilityDeliver
     */
    private void calculateOutgoingGoodsEarnings(int ordered, Round round, Facility facilityDeliver) {
        int budgetDeliver = round.getRemainingBudgetByFacility(facilityDeliver);
        int orderCostDeliver = ordered * facilityDeliver.getFacilityType().getValueOutgoingGoods();

        round.updateRemainingBudget(budgetDeliver + orderCostDeliver, facilityDeliver);
    }

    /**
     * Calculate the costs the facility needs to pay for receiving goods.
     * @param ordered
     * @param round
     * @param facilityOrder
     */
    private void calculateIncomingGoodsCosts(int ordered, Round round, Facility facilityOrder) {
        int budgetOrder = round.getRemainingBudgetByFacility(facilityOrder);
        int orderCostOrder = ordered * facilityOrder.getFacilityType().getValueIncomingGoods();

        round.updateRemainingBudget(budgetOrder - orderCostOrder, facilityOrder);
    }

    /**
     * Get the back orders and count it on the amount ordered.
     * @param ordered
     * @param round
     * @param facilityOrder
     * @param facilityDeliver
     * @return
     */
    private int dealWithBackOrders(int ordered, Round round, Facility facilityOrder, Facility facilityDeliver) {
        if (round.isTurnBackLogFilledByFacility(facilityOrder)) {
            int backOrderAmount = round.getTurnBacklogByFacility(facilityOrder, facilityDeliver);
            round.updateTurnBacklogByFacility(facilityOrder, facilityDeliver, 0);
            return ordered + backOrderAmount;
        }

        return ordered;
    }
}
