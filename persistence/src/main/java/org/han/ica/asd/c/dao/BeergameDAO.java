package org.han.ica.asd.c.dao;

import org.han.ica.asd.c.model.Beergame;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;


import static org.han.ica.asd.c.dbconnection.DBConnection.RollBackTransaction;
import static org.han.ica.asd.c.dbconnection.DBConnection.connect;

public class BeergameDAO {
    //CRUD - Create, read, update, delete
    private static final String CREATE_BEERGAME = "INSERT INTO Beergame(GameName, GameDate) VALUES (?,?)";
    private static final String READ_BEERGAME = "SELECT GameName, GameDate FROM Beergame";
    //Updating is not possible for this table
    private static final String DELETE_BEERGAME = "DELETE FROM Beergame WHERE GameName = ? AND GameDate = ? AND GameEndDate = ?";


    /**
     * A method which creates a BeerGame in the Database
      * @param gameName The specified name of the game
     */
    public void createBeergame(String gameName) {
        Connection conn = null;
        try {
            conn = connect();
            if (conn == null) return;
            try (PreparedStatement pstmt = conn.prepareStatement(CREATE_BEERGAME)) {

                conn.setAutoCommit(false);

                pstmt.setString(1, gameName);
                pstmt.setString(2, new Date().toString());

                pstmt.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            RollBackTransaction(conn);
        }
    }

    /**
     * A method which returns all BeerGames which are inserted in the database
     * @return An Arraylist of BeerGames
     */
    public ArrayList<Beergame> readBeergames() {
        Connection conn = null;
        ArrayList<Beergame> beerGames = new ArrayList<>();
        try {
            conn = connect();
            if (conn == null) return null;
            try (PreparedStatement pstmt = conn.prepareStatement(READ_BEERGAME)) {

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        beerGames.add(new Beergame(rs.getString("GameName"), rs.getString("GameDate"), rs.getString("GameEndDate")));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return beerGames;
    }

    /**
     * Deletes a BeerGame from the SQLite Database
     * @param gameName The specified name of the game which needs to be deleted
     * @param gameDate The specified date of the game which needs to be deleted
     */
    public void deleteBeergame(String gameName, String gameDate, String gameEndDate) {
        Connection conn = null;
        try {
            conn = connect();
            if (conn == null) return;
            try (PreparedStatement pstmt = conn.prepareStatement(DELETE_BEERGAME)) {

                conn.setAutoCommit(false);

                pstmt.setString(1, gameName);
                pstmt.setString(2, gameDate);
                pstmt.setString(3, gameEndDate);

                pstmt.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            RollBackTransaction(conn);
        }
    }
}
