package org.han.ica.asd.c.replay_data.fakes;

import org.han.ica.asd.c.interfaces.replay.IRetrieveReplayData;
import org.han.ica.asd.c.model.domain_objects.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/***
 * Filler class for Guice. Will be replaced with actual implementation in the Persistance Layer
 */
public class TMP implements IRetrieveReplayData{
    @Override
    public List<Facility> getAllFacilities() {
        return new ArrayList<>();
    }

    @Override
    public List<Round> getAllRounds() {
        return new ArrayList<>();
    }
}
