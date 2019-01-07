package org.han.ica.asd.c.leadermigration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Wrapper for system library based IP retrieval.
 */
public class IpHandler {

	/**
	 * Empty constructor.
	 */
	public IpHandler() {
		// default
	}

	/**
	 * Retrieve the local IP.
	 * @return the IP in a string.
	 * @throws UnknownHostException thrown when the system library has an issue with finding localhost.
	 */
	public String getLocalIp() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostAddress();
	}
}
