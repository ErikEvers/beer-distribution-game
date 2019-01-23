package org.han.ica.asd.c.interfaces.communication;

public interface IConnector {
    /**
     * Register an instance of a IMessageObserver as an observer that listens to the communication component.
     * @param observer, instance of IMessageObserver.
     */
    void addObserver(IConnectorObserver observer);
    void removeObserver(IConnectorObserver observer);
}
