package org.han.ica.asd.c.gui_replay_game.replay_game_controller;

import javafx.scene.control.TextFormatter;

public class NumericTextFormatter {
    private static TextFormatter textFormatter;

    public static TextFormatter getTextFormatter(){
        setuptextFormatter();
        return textFormatter;
    }

    private static void setuptextFormatter() {
        textFormatter = new TextFormatter<>(change -> {
            if (!change.isContentChange()) {
                return change;
            }

            sanitizeInput(change);

            return change;
        });
    }

    private static void sanitizeInput(TextFormatter.Change change) {
        String text = change.getControlNewText();
        if (!(text.matches("[0-9]+"))) {
            change.setText(change.getText().replaceAll("[^\\d.]", ""));
        }
    }
}
