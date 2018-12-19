package org.han.ica.asd.c.exceptions;

/**
 * Exception that gets thrown whenever there are issue with finding a particular player during the election process.
 */
public class PlayerNotFoundException extends Exception {

	/**
	 * Empty constructor.
	 */
  public PlayerNotFoundException() {
		// default
	}

	/**
	 * Constructor which accepts a message.
	 * @param exception the string containing the message.
	 */
  public PlayerNotFoundException(String exception) {
    super(exception);
  }
}
