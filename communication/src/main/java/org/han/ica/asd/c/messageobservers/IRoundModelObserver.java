package org.han.ica.asd.c.messageobservers;


import org.han.ica.asd.c.model.Round;

public interface IRoundModelObserver extends IGameMessageObserver{
    void roundModelReceived(Round roundModel);
}
