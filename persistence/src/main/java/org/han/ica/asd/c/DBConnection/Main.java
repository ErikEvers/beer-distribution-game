package org.han.ica.asd.c.DBConnection;

import DAO.BeergameDAO;
import Models.Beergame;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        BeergameDAO beergameDAO = new BeergameDAO();
        beergameDAO.createBeergame("Nieuwe game man!");
        ArrayList<Beergame> games = beergameDAO.readBeergames();

        for(Beergame game : games){
            System.out.println(game.getGameName() + "\t" + game.getGameDate() + "\t" + game.getGameEndDate());
        }
    }
}
