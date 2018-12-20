package org.han.ica.asd.c.businessrule.mocks;

import org.han.ica.asd.c.gamevalue.GameValue;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.Round;

import java.util.Map;

public class GameData {
    private Round round;
    private Map<Facility,Map<Facility,Integer>> turnOrder;
    private Map<Facility,Map<Facility,Integer>> turnDeliver;
    private Map<Facility,Map<Facility,Integer>> turnReceived;
    private Map<Facility,Map<Facility,Integer>> turnBackOrder;
    private Map<Facility,Integer> turnStock;
    private Map<Facility,Integer> remainingBudget;


}
