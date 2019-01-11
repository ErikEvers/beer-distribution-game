package org.han.ica.asd.c.businessrule.parser.ast;

import org.apache.ibatis.javassist.NotFoundException;
import org.han.ica.asd.c.businessrule.parser.ast.action.Action;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.gamevalue.GameValue;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRuleStore;
import org.han.ica.asd.c.model.domain_objects.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.han.ica.asd.c.gamevalue.GameValue.HIGHEST;
import static org.han.ica.asd.c.gamevalue.GameValue.LOWEST;

public class Replacer {

    private static final String HAS_CHARACTERS = "[a-zA-Z ]+";
    private NodeConverter nodeConverter;
    private static final Logger LOGGER = Logger.getLogger(Replacer.class.getName());

    private IBusinessRuleStore businessRuleStore;

    @Inject
    Replacer(NodeConverter nodeConverter,  @Named("BusinessruleStore")IBusinessRuleStore businessRuleStore) {
        this.nodeConverter = nodeConverter;
        this.businessRuleStore = businessRuleStore;
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

    private int getHighest(GameValue attribute, GameValue facility, Round round) throws NotFoundException {
        List<String> facilityTypeList = getDesiredFacilities(facility);
        Comparator<FacilityTurn> facilityTurnComparator = null;
        Comparator<FacilityTurnOrder> facilityTurnOrderComparator = null;
        Comparator<FacilityTurnDeliver> facilityTurnDeliverComparator = null;

        FacilityTurn facilityTurn = null;
        FacilityTurnOrder facilityTurnOrder = null;
        FacilityTurnDeliver facilityTurnDeliver = null;
        String notFound = "the identifier is not found";
        switch (attribute) {
            case ORDERED:
                //select id van factory met de hoogste ordered.
                break;
            case STOCK:
                Comparator<FacilityTurn> comparatorStock = Comparator.comparing( FacilityTurn::getStock );
                facilityTurn = round.getFacilityTurns().stream().filter(i ->
                facilityTypeList.contains(String.valueOf(i.getFacilityId()))).max(comparatorStock).orElse(null);
                if(facilityTurn!=null) {
                    return facilityTurn.getFacilityId();
                }
                throw new NotFoundException(notFound);
            case BUDGET:
                Comparator<FacilityTurn> comparator = Comparator.comparing( FacilityTurn::getRemainingBudget );
                facilityTurn = round.getFacilityTurns().stream().filter(i ->
                        facilityTypeList.contains(String.valueOf(i.getFacilityId()))).max(comparator).orElse(null);
                if(facilityTurn!=null) {
                    return facilityTurn.getFacilityId();
                }
                throw new NotFoundException(notFound);
            case BACKLOG:
                break;
            case INCOMINGORDER:
                break;
            case OUTGOINGGOODS:
                break;
            default:
                throw new NotFoundException(notFound);
        }
        return 0;
    }

    private List<String> getDesiredFacilities(GameValue facilityType) {
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
                try {
                    throw new NotFoundException("Cannot find the specified facility");
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }

    public void replacePerson(Action action, Round round, GameValue facilityType, GameValue attribute, GameValue highestOrLowest) {
        if (HIGHEST.equals(highestOrLowest)) {
            try {
                action.replacePerson(getHighest(attribute, facilityType, round));
            } catch (NotFoundException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        } else if (LOWEST.equals(highestOrLowest)) {

            return;
        }
    }
}
