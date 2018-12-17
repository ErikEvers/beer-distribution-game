package org.han.ica.asd.c;

import javafx.scene.control.Alert;

public class Global {
    public final static int Port = 4445;
    public final static int FaultDetectionInterval = 10000;



    public static void ShowAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
