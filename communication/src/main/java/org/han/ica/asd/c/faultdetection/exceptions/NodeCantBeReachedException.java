package org.han.ica.asd.c.faultdetection.exceptions;

import org.han.ica.asd.c.faultdetection.FaultDetectionClient;

/**
 * <h2>Is used for throwing a custom exception. Mainly thrown in the 'FaultDetectionClient'.</h2>
 * <p>This exception is used for exception handling. Example: when the 'FaultDetectionClient' tries to make a connection
 * with a node but it fails, this exception is thrown. This way the 'FaultDetectionClient' can keep count of the amount
 * of times it was unable to make a connection with a specific node.</p>
 *
 * @author oscar
 * @see FaultDetectionClient
 */

public class NodeCantBeReachedException extends Exception {
    public NodeCantBeReachedException() {
        super();
    }

    public NodeCantBeReachedException(Throwable cause) {
        super(cause);
    }
}
