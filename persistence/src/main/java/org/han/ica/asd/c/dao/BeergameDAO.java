package org.han.ica.asd.c.dao;


import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.domain_objects.BeerGame;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.UUID.randomUUID;


public class BeergameDAO {
	private static final String CREATE_BEERGAME = "INSERT INTO Beergame(GameId, GameName, GameDate) VALUES (?,?,?);";
	private static final String READ_BEERGAMES = "SELECT * FROM Beergame;";
	private static final String READ_BEERGAME = "SELECT * FROM Beergame WHERE GameId = ?;";
	private static final String READ_ONGOING_BEERGAME = "SELECT * FROM Beergame WHERE GameEndDate = '' OR GameEndDate = null;";
	private static final String DELETE_BEERGAME = "DELETE FROM Beergame WHERE GameId = ?;";
	private static final Logger LOGGER = Logger.getLogger(BeergameDAO.class.getName());

	@Inject
	private IDatabaseConnection databaseConnection;

	@Inject
	private ConfigurationDAO configurationDAO;

	@Inject
	private GameAgentDAO gameAgentDAO;

	@Inject
	private RoundDAO roundDAO;

	@Inject
	private PlayerDAO playerDAO;




	public BeergameDAO(){
		//Empty Constructor for GUICE
	}

	/**
	 * A method which creates a BeerGameDB in the Database
	 *
	 * @param gameName The specified name of the game
	 */
	public void createBeergame(String gameName) {
		String uuid = randomUUID().toString();
		Connection conn = databaseConnection.connect();
		if (conn != null) {
			try (PreparedStatement pstmt = conn.prepareStatement(CREATE_BEERGAME)) {

				conn.setAutoCommit(false);
				pstmt.setString(1, uuid);
				pstmt.setString(2, gameName);
				pstmt.setString(3, new Date().toString());

				pstmt.executeUpdate();
				DaoConfig.setCurrentGameId(uuid);
				conn.commit();
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, e.toString(), e);
				databaseConnection.rollBackTransaction(conn);
			}
		}
	}

	/**
	 * A method which returns all BeerGames which are inserted in the database
	 *
	 * @return An Arraylist of BeerGames
	 */
	public List<BeerGame> readBeergames() {
		Connection conn = databaseConnection.connect();
		ArrayList<BeerGame> beerGames = new ArrayList<>();
		if (conn != null) {
			try (PreparedStatement pstmt = conn.prepareStatement(READ_BEERGAMES); ResultSet rs = pstmt.executeQuery()) {
				conn.setAutoCommit(false);
				while (rs.next()) {
					beerGames.add(new BeerGame(rs.getString("GameId"), rs.getString("GameName"), rs.getString("GameDate"), rs.getString("GameEndDate")));
				}
				conn.commit();
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, e.toString(),e);
				databaseConnection.rollBackTransaction(conn);
			}
		}
		return beerGames;
	}

	/**
	 * Deletes a BeerGameDB from the SQLite Database
	 * @param gameId The specified Id of the game which needs to be deleted
	 */
	public void deleteBeergame(String gameId) {
		Connection conn = databaseConnection.connect();
		if (conn != null) {
			try (PreparedStatement pstmt = conn.prepareStatement(DELETE_BEERGAME)) {

				conn.setAutoCommit(false);

				pstmt.setString(1, gameId);

				pstmt.executeUpdate();
				conn.commit();
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, e.toString(),e);
				databaseConnection.rollBackTransaction(conn);
			}
		}
	}

	/**
	 * A method which returns a single beergame
	 * @return A beergame object
	 */
	public BeerGame getGameLog() {
		Connection conn = databaseConnection.connect();
		BeerGame beergame = null;
		beergame = getBeerGame(conn, beergame, READ_BEERGAME);
		return beergame;
	}

	/**
	 * A method which returns a single ongoing beergame
	 * @return A beergame object
	 */
	public BeerGame getGameLogFromOngoingGame() {
		Connection conn = databaseConnection.connect();
		BeerGame beergame = null;
		beergame = getBeerGame(conn, beergame, READ_ONGOING_BEERGAME);
		return beergame;
	}

	private BeerGame getBeerGame(Connection conn, BeerGame beergame, String readOngoingBeergame) {
		if (conn != null) {
			try (PreparedStatement pstmt = conn.prepareStatement(readOngoingBeergame)) {
				conn.setAutoCommit(false);
				pstmt.setString(1, DaoConfig.getCurrentGameId());
				try (ResultSet rs = pstmt.executeQuery()) {
					if(!rs.isClosed()) {
						beergame = new BeerGame(rs.getString("GameId"), rs.getString("GameName"), rs.getString("GameDate"), rs.getString("GameEndDate"));
						beergame.setConfiguration(configurationDAO.readConfiguration());
						beergame.setAgents(gameAgentDAO.readGameAgentsForABeerGame());
						beergame.setRounds(roundDAO.getRounds());
						beergame.setPlayers(playerDAO.getAllPlayers());
					}
				}
				conn.commit();
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, e.toString(),e);
			}
		}
		return beergame;
	}


}


