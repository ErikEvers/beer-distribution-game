package org.han.ica.asd.c.gamelogic.participants.fakes;

import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.Player;

public class PlayerFake extends Player {
    public PlayerFake() {
        super("", "", new Facility(
                null,
                null,
                null, 1), "", true);
    }
}
