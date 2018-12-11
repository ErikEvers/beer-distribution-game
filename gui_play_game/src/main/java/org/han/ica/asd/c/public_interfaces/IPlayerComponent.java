package org.han.ica.asd.c.public_interfaces;

import org.han.ica.asd.c.domain.Agent;
import org.han.ica.asd.c.domain.Facility;
import org.han.ica.asd.c.domain.Player;

import java.util.List;

public interface IPlayerComponent {
    void insertPlayer(Player player);
    void insertAgent(Agent agent);
    void seeOtherFacilities(List<Facility> facilities);
    void placeOrder(int amount);
}
