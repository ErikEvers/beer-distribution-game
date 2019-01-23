package org.han.ica.asd.c.fxml_helper;

import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public class NumberInputFormatter {
	/**
	 * Retrieves a formatter that can be applied to text fields to ensure numbers only
	 * @return
	 * The text formatter
	 */
	static public UnaryOperator<TextFormatter.Change> getChangeUnaryOperator() {
		return change -> {
			String newText = change.getControlNewText();
			if (newText.matches("[0-9]+")){
				return change;
			}
			if (newText.length() < 1) {
				change.setText("0");
				change.setCaretPosition(change.getText().length());
				return change;
			}
			return null;
		};
	}
}
