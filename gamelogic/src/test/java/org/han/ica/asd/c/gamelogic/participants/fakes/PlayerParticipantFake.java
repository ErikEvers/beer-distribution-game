package org.han.ica.asd.c.gamelogic.participants.fakes;

import org.han.ica.asd.c.gamelogic.participants.domain_models.PlayerParticipant;
import org.han.ica.asd.c.model.domain_objects.Facility;

public class PlayerParticipantFake extends PlayerParticipant {
    public PlayerParticipantFake() {
        super(
                "",
                "",
                new Facility(
                        null,
                        null,
                        null, 1),
                "",
                true);
    }
}
