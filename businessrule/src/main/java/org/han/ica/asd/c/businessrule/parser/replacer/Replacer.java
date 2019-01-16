package org.han.ica.asd.c.businessrule.parser.replacer;

import org.apache.ibatis.javassist.NotFoundException;
import org.han.ica.asd.c.businessrule.parser.ast.action.Action;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.gamevalue.GameValue;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRuleStore;
import org.han.ica.asd.c.model.domain_objects.FacilityTurn;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnDeliver;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnOrder;
import org.han.ica.asd.c.model.domain_objects.IFacility;
import org.han.ica.asd.c.model.domain_objects.Round;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Replacer {

    private static final String HAS_CHARACTERS = "[a-zA-Z ]+";
    private NodeConverter nodeConverter;
    private static final Logger LOGGER = Logger.getLogger(Replacer.class.getName());
    private static final String THE_IDENTIFIER_IS_NOT_FOUND = "The identifier is not found";
    private static final String THE_FACILITY_IS_NOT_FOUND = "The facility is not found";
    private IBusinessRuleStore businessRuleStore;

    @Inject
    public Replacer(NodeConverter nodeConverter, IBusinessRuleStore businessRuleStore) {
        this.nodeConverter = nodeConverter;
        this.businessRuleStore = businessRuleStore;
    }

    public Replacer() {

    }

    /***
     * Gets one part of the value replaces it with game data
     *
     * @param value the value
     * @param round the previous round
     * @param facilityId the id of the facility
     */
    public void replace(Value value, Round round, int facilityId) {
        String replacementValue;
        if (value.getValue().size() > 1) {
            String secondVariable = value.getSecondPartVariable();
            if (GameValue.checkIfFacility(secondVariable) || Pattern.matches(HAS_CHARACTERS, value.getSecondPartVariable())) {
                replaceOnVariable(value, round, facilityId, secondVariable);
            }
        }
        replacementValue = value.getFirstPartVariable();
        if (Pattern.matches(HAS_CHARACTERS, value.getFirstPartVariable())) {
            replaceOnVariable(value, round, facilityId, replacementValue);
        }
    }

    /***
     * Replaces exactly one part of the variable
     *
     * @param value the value
     * @param round the previous round
     * @param facilityId the id of the facility
     * @param variable one part of value
     */
    private void replaceOnVariable(Value value, Round round, int facilityId, String variable) {
        GameValue gameValue = getGameValue(variable);
        String newReplacementValue;
        if (GameValue.checkIfFacility(variable)) {
            newReplacementValue = String.valueOf(nodeConverter.getFacilityId(variable));
            value.replaceValueWithValue(newReplacementValue);
        } else if (gameValue != null) {
            newReplacementValue = getReplacementValue(gameValue, round, facilityId);
            value.replaceValueWithValue(newReplacementValue);
        }
    }

    /***
     * If the variable is a variable then it returns the corresponding game value
     *
     * @param variable one part of the value
     * @return the corresponding game value
     */
    public GameValue getGameValue(String variable) {
        for (GameValue gameValue : GameValue.values()) {
            if (gameValue.contains(variable)) {
                return gameValue;
            }
        }
        return null;
    }

    /***
     * Gets the replacementValue from the previous round
     *
     * @param gameValue the type of game value
     * @param round from the previous round
     * @param facilityId the id of the facility
     * @return the replacement Value
     */
    private String getReplacementValue(GameValue gameValue, Round round, int facilityId) {
        switch (gameValue) {
            case ORDERED:
                return getOrder(round, facilityId);
            case STOCK:
                return getStock(round, facilityId);
            case BUDGET:
                return getBudget(round, facilityId);
            case BACKLOG:
                return getBacklog(round, facilityId);
            case INCOMINGORDER:
                return getIncomingOrder(round, facilityId);
            case OUTGOINGGOODS:
                return getOutgoingGoods(round, facilityId);
            case ROUND:
                return String.valueOf(round.getRoundId());
            default:
                return "";
        }
    }

    /***
     * Gets the number of orders of an facility
     * @param round the given round
     * @param facilityId the id of the given facility
     * @return the order amount
     */
    private String getOrder(Round round, int facilityId) {
        for (FacilityTurnOrder facilityTurnOrder : round.getFacilityOrders()) {
            if (facilityTurnOrder.getFacilityId() == facilityId) {
                return String.valueOf(facilityTurnOrder.getOrderAmount());
            }
        }
        return "";
    }

    /***
     * Gets the stock of an facility
     * @param round the given round
     * @param facilityId the id of the given facility
     * @return stock
     */
    private String getStock(Round round, int facilityId) {
        for (FacilityTurn facilityTurn : round.getFacilityTurns()) {
            if (facilityTurn.getFacilityId() == facilityId) {
                return String.valueOf(facilityTurn.getStock());
            }
        }
        return "";
    }

    /***
     * Gets the remaining budget of an facility
     * @param round the given round
     * @param facilityId the id of the given facility
     * @return budget
     */
    private String getBudget(Round round, int facilityId) {
        for (FacilityTurn facilityTurn : round.getFacilityTurns()) {
            if (facilityTurn.getFacilityId() == facilityId) {
                return String.valueOf(facilityTurn.getRemainingBudget());
            }
        }
        return "";
    }

    /***
     * Gets the number of open orders of an facility
     * @param round the given round
     * @param facilityId the id of the given facility
     * @return budget
     */
    private String getBacklog(Round round, int facilityId) {
        for (FacilityTurn facilityTurn : round.getFacilityTurns()) {
            if (facilityTurn.getFacilityId() == facilityId) {
                return String.valueOf(facilityTurn.getBackorders());
            }
        }
        return "";
    }

    /***
     * Gets the incoming order of an facility
     * @param round the given round
     * @param facilityId the id of the given facility
     * @return incoming order amount
     */
    private String getIncomingOrder(Round round, int facilityId) {
        for (FacilityTurnOrder facilityTurn : round.getFacilityOrders()) {
            if (facilityTurn.getFacilityIdOrderTo() == facilityId) {
                return String.valueOf(facilityTurn.getOrderAmount());
            }
        }
        return "";
    }

    /***
     * Gets the outgoing order of an facility
     * @param round the given round
     * @param facilityId the id of the given facility
     * @return outgoing goods amount
     */
    private String getOutgoingGoods(Round round, int facilityId) {
        for (FacilityTurnDeliver facilityTurnDeliver : round.getFacilityTurnDelivers()) {
            if (facilityTurnDeliver.getFacilityId() == facilityId) {
                return String.valueOf(facilityTurnDeliver.getDeliverAmount());
            }
        }
        return "";
    }

    /***
     * Gets the facility id based on the highest or lowest and the attribute
     * @param attribute like inventory,
     * @param facility the current facility
     * @param round the round
     * @param highestOrLowest if the filter selects the highest or the lowest
     * @return the facility id
     * @throws NotFoundException
     */
    private int getExtreme(GameValue attribute, GameValue facility, Round round, GameValue highestOrLowest) throws NotFoundException {
        switch (attribute) {
            case ORDERED:
                return getFacilityIdBasedOnExtreme(round.getFacilityOrders(), highestOrLowest, getDesiredFacilities(facility), Comparator.comparing(FacilityTurnOrder::getOrderAmount));
            case STOCK:
                return getFacilityIdBasedOnExtreme(round.getFacilityTurns(), highestOrLowest, getDesiredFacilities(facility), Comparator.comparing(FacilityTurn::getStock));
            case BUDGET:
                return getFacilityIdBasedOnExtreme(round.getFacilityTurns(), highestOrLowest, getDesiredFacilities(facility), Comparator.comparing(FacilityTurn::getRemainingBudget));
            case BACKLOG:
                return getFacilityIdBasedOnExtreme(round.getFacilityTurns(), highestOrLowest, getDesiredFacilities(facility), Comparator.comparing(FacilityTurn::getBackorders));
            case INCOMINGORDER:
                return getFacilityIdBasedOnExtreme(round.getFacilityOrders(), highestOrLowest, getDesiredFacilities(facility), Comparator.comparing(FacilityTurnOrder::getOrderAmount));
            case OUTGOINGGOODS:
                return getFacilityIdBasedOnExtreme(round.getFacilityTurnDelivers(), highestOrLowest, getDesiredFacilities(facility), Comparator.comparing(FacilityTurnDeliver::getDeliverAmount));
            default:
                throw new NotFoundException(THE_IDENTIFIER_IS_NOT_FOUND);
        }
    }

    /***
     *
     * @param facilityList
     * @param highestOrLowest
     * @param facilityTypeList
     * @param facilityComparator the comparator
     * @param <T> An object which implements IFacility
     * @return the found facility id
     * @throws NotFoundException
     */
    private <T extends IFacility> int getFacilityIdBasedOnExtreme(List<T> facilityList, GameValue highestOrLowest, List<String> facilityTypeList, Comparator<T> facilityComparator) throws NotFoundException {
        T facilityTurnOrder;
        Stream<T> stream = facilityList.stream().filter(i ->
                facilityTypeList.contains(String.valueOf(i.getFacilityId())));

        if (highestOrLowest.equals(GameValue.HIGHEST)) {
            facilityTurnOrder = stream.max(facilityComparator).orElse(null);
        } else {
            facilityTurnOrder = stream.min(facilityComparator).orElse(null);
        }
        if (facilityTurnOrder != null) {
            return facilityTurnOrder.getFacilityId();
        }
        throw new NotFoundException(THE_IDENTIFIER_IS_NOT_FOUND);
    }

    /**
     * Gets all the facilities of the game
     *
     * @param facilityType the desired facilities
     * @return a list of facilities
     * @throws NotFoundException
     */
    private List<String> getDesiredFacilities(GameValue facilityType) throws NotFoundException {
        switch (facilityType) {
            case FACTORY:
                return businessRuleStore.getAllFacilities().get(0);
            case WHOLESALER:
                return businessRuleStore.getAllFacilities().get(1);
            case REGIONALWAREHOUSE:
                return businessRuleStore.getAllFacilities().get(2);
            case RETAILER:
                return businessRuleStore.getAllFacilities().get(3);
            default:
                throw new NotFoundException(THE_FACILITY_IS_NOT_FOUND);
        }
    }

    /***
     * Replaces the person with the found facility id
     * @param action the corresponding action
     * @param round the round
     * @param facilityType the desired facility type
     * @param attribute an attribute like inventory
     * @param highestOrLowest
     */
    public void replacePerson(Action action, Round round, GameValue facilityType, GameValue attribute, GameValue highestOrLowest) {
        try {
            action.replacePerson(getExtreme(attribute, facilityType, round, highestOrLowest));
        } catch (NotFoundException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }
}
