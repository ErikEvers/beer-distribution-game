package org.han.ica.asd.c.interfaces.gui_play_game;

import org.han.ica.asd.c.model.domain_objects.Round;

public interface IPlayGame {
    void refreshInterfaceWithCurrentStatus(Round status);
}
