package org.han.ica.asd.c.gamelogic.roundcalculator;

import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.FacilityTurn;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnDeliver;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnOrder;
import org.han.ica.asd.c.model.domain_objects.FacilityType;
import org.han.ica.asd.c.model.domain_objects.Round;

import java.util.List;

public class RoundCalculator {

    public RoundCalculator() {
    }

    public Round calculateRound(Round round, BeerGame beerGame) {
        Round outcome = new Round();

        outcome.setRoundId(round.getRoundId() + 1);

        List<FacilityTurn> outcomeList = round.getFacilityTurns();

        int lower = beerGame.getConfiguration().getMinimalOrderRetail();
        int upper = beerGame.getConfiguration().getMaximumOrderRetail();

        for(FacilityTurn facilityTurn : outcomeList) {
            FacilityType facilityType = beerGame.getFacilityById(facilityTurn.getFacilityId()).getFacilityType();

            if(facilityType.getFacilityName().equals("Retailer")) {
                round.getFacilityOrders().add(new FacilityTurnOrder(
                    facilityTurn.getFacilityId(),
                    facilityTurn.getFacilityId(),
                    ((int)(Math.random() * (upper - lower)) + lower)
                ));

                if(facilityTurn.getBackorders() > 0) {
                    round.getFacilityTurnDelivers().add(new FacilityTurnDeliver(
                        facilityTurn.getFacilityId(),
                        facilityTurn.getFacilityId(),
                        0,
                        facilityTurn.getBackorders()
                    ));
                }
            }
        }

        for(FacilityTurnDeliver facilityTurnDeliver : round.getFacilityTurnDelivers()) {
            FacilityTurn curDeliverer = outcomeList.stream().filter(facilityTurn -> facilityTurn.getFacilityId() == facilityTurnDeliver.getFacilityId()).findFirst().orElse(null);
            FacilityTurn curDeliveree = outcomeList.stream().filter(facilityTurn -> facilityTurn.getFacilityId() == facilityTurnDeliver.getFacilityIdDeliverTo()).findFirst().orElse(null);


            curDeliverer.setStock(curDeliverer.getStock() - facilityTurnDeliver.getDeliverAmount());

            if (curDeliverer.getStock() < 0) {
                int possibleDelivery = curDeliverer.getStock() + facilityTurnDeliver.getDeliverAmount();
                facilityTurnDeliver.setDeliverAmount(possibleDelivery);
                curDeliverer.setStock(0);
            }

            curDeliverer.setBackorders(curDeliverer.getBackorders() - facilityTurnDeliver.getDeliverAmount());
            curDeliverer.setRemainingBudget(calculateOutgoingGoodsEarnings(curDeliverer, beerGame.getFacilityById(curDeliverer.getFacilityId()).getFacilityType(), facilityTurnDeliver.getDeliverAmount()));

            if(facilityTurnDeliver.getFacilityId() != facilityTurnDeliver.getFacilityIdDeliverTo()) {
                curDeliveree.setStock(curDeliveree.getStock() + facilityTurnDeliver.getDeliverAmount());
                curDeliverer.setRemainingBudget(calculateIncomingGoodsCosts(curDeliveree, beerGame.getFacilityById(curDeliveree.getFacilityId()).getFacilityType(), facilityTurnDeliver.getDeliverAmount()));
            }
        }

        for(FacilityTurnOrder facilityTurnOrder : round.getFacilityOrders()) {
            FacilityTurn curFacility = outcomeList.stream().filter(facilityTurn -> facilityTurn.getFacilityId() == facilityTurnOrder.getFacilityIdOrderTo()).findFirst().orElse(null);
            if((facilityTurnOrder.getFacilityId() != facilityTurnOrder.getFacilityIdOrderTo()) || beerGame.getFacilityById(facilityTurnOrder.getFacilityId()).getFacilityType().getFacilityName().equals("Retailer")) {
                curFacility.setBackorders(curFacility.getBackorders() + facilityTurnOrder.getOrderAmount());
            } else {
                curFacility.setStock(curFacility.getStock() + facilityTurnOrder.getOrderAmount());
            }
        }


        for(FacilityTurn facilityTurn : outcomeList) {

            facilityTurn.setBankrupt(facilityTurn.getRemainingBudget() > 0);

            FacilityType facilityType = beerGame.getFacilityById(facilityTurn.getFacilityId()).getFacilityType();

            updateRemainingBudget(facilityTurn, facilityType);

            outcome.getFacilityTurns().add(new FacilityTurn(
                facilityTurn.getFacilityId(),
                outcome.getRoundId(),
                facilityTurn.getStock(),
                facilityTurn.getBackorders(),
                facilityTurn.getRemainingBudget(),
                facilityTurn.isBankrupt()
            ));
        }

        return outcome;
    }

    /**
     * Calculates the new remaining budget.
     * The remaining budget for facility Order gets calculated on basis of the stock.
     * The remaining budget for the facility Deliver gets calculated on basis of the backOrders it has.
     * @param facilityTurn
     */
    private void updateRemainingBudget(FacilityTurn facilityTurn, FacilityType facilityType) {
        facilityTurn.setRemainingBudget(calculateStockCost(facilityTurn, facilityType));
        facilityTurn.setRemainingBudget(calculateBackLogCost(facilityTurn, facilityType));
    }

    /**
     * Calculate the stock cost.
     * @param facilityTurn
     * @param facilityType
     * @return
     */
    private int calculateStockCost(FacilityTurn facilityTurn, FacilityType facilityType) {
        int remainingBudget = facilityTurn.getRemainingBudget();
        int stock = facilityTurn.getStock();
        int stockCosts = stock * facilityType.getStockHoldingCosts();
        return (remainingBudget - stockCosts);
    }

    /**
     * Because of the structure of our maps it's not possible to calculate the stock and the backlog cost for the same facility in the same function.
     * @param facilityTurn
     * @param facilityType
     * @return
     */
    private int calculateBackLogCost(FacilityTurn facilityTurn, FacilityType facilityType) {
        int remainingBudgetFacilityDeliver = facilityTurn.getRemainingBudget();
        int backOrders = facilityTurn.getBackorders();

        if (backOrders > 0) {
            int backlogCosts = backOrders * facilityType.getOpenOrderCosts();
            return (remainingBudgetFacilityDeliver - backlogCosts);
        }

        return remainingBudgetFacilityDeliver;
    }

    /**
     * Calculate the outgoing going goods value which is being earned by the facility deliver.
     * @param facilityTurn
     * @param facilityType
     */
    private int calculateOutgoingGoodsEarnings(FacilityTurn facilityTurn, FacilityType facilityType, int deliverAmount) {
        return facilityTurn.getRemainingBudget() + (deliverAmount * facilityType.getValueOutgoingGoods());
    }

    /**
     * Calculate the costs the facility needs to pay for receiving goods.
     * @param facilityTurn
     * @param facilityType
     */
    private int calculateIncomingGoodsCosts(FacilityTurn facilityTurn, FacilityType facilityType, int orderAmount) {
        return facilityTurn.getRemainingBudget() - (orderAmount * facilityType.getValueIncomingGoods());
    }
}
