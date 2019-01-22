package org.han.ica.asd.c.dao;


import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityType;

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
	private static final String CREATE_BEERGAME_FROM_MODEL = "INSERT INTO Beergame(GameId, GameName, GameDate,GameEndDate) VALUES (?,?,?,?);";
	private static final String READ_BEERGAMES = "SELECT * FROM Beergame;";
	private static final String READ_BEERGAME = "SELECT * FROM Beergame WHERE GameId = ?;";
	private static final String READ_ONGOING_BEERGAME = "SELECT * FROM Beergame WHERE GameEndDate IS NULL;";
	private static final String DELETE_BEERGAME = "DELETE FROM Beergame WHERE GameId = ?;";
	private static final String UPDATE_ENDDATE = "UPDATE Beergame SET GameEndDate = ? WHERE GameId = ?;";
	private static final Logger LOGGER = Logger.getLogger(BeergameDAO.class.getName());
	public static final String GAME_ID = "GameId";
	public static final String GAME_NAME = "GameName";
	public static final String GAME_DATE = "GameDate";
	public static final String GAME_END_DATE = "GameEndDate";

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

	@Inject
	private LeaderDAO leaderDAO;

	@Inject
	private FacilityDAO facilityDAO;

	@Inject
	private FacilityTypeDAO facilityTypeDAO;




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
				conn.close();
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, e.toString(), e);
				databaseConnection.rollBackTransaction(conn);
			}
		}
	}

	/**
	 * Creates a beergame object in the database with a Beergame Model
	 * @param beerGame A beergame which needs to be inserted
	 */
	public void createBeergame(BeerGame beerGame) {
		if(DaoConfig.getCurrentGameId() != null){
			updateBeergame(beerGame);
		}
		else{
		Connection conn = databaseConnection.connect();
		if (conn != null) {
			try (PreparedStatement pstmt = conn.prepareStatement(CREATE_BEERGAME_FROM_MODEL)) {

				conn.setAutoCommit(false);
				pstmt.setString(1, beerGame.getGameId());
				pstmt.setString(2, beerGame.getGameName());
				pstmt.setString(3, beerGame.getGameDate());
				pstmt.setString(4, beerGame.getGameEndDate());
				pstmt.executeUpdate();
				conn.commit();
				DaoConfig.setCurrentGameId(beerGame.getGameId());

				beerGame.getConfiguration().getFacilities().forEach(facility -> {
					facilityDAO.createFacility(facility);
					facilityTypeDAO.createFacilityType(facility.getFacilityType());
				});

				configurationDAO.createConfiguration(beerGame.getConfiguration());
				roundDAO.insertRounds(beerGame.getRounds());
				playerDAO.insertPlayers(beerGame.getPlayers());
				gameAgentDAO.insertGameAgents(beerGame.getAgents());
				leaderDAO.insertLeader(beerGame.getLeader());
				conn.close();

				} catch (SQLException e) {
					LOGGER.log(Level.SEVERE, e.toString(), e);
					databaseConnection.rollBackTransaction(conn);
				}
			}
		}
	}

	public void updateBeergame(BeerGame beerGame) {
		configurationDAO.updateConfigurations(beerGame.getConfiguration());
		roundDAO.updateRounds(beerGame.getRounds());
		playerDAO.updatePlayers(beerGame.getPlayers());
		gameAgentDAO.updateGameagents(beerGame.getAgents());
		leaderDAO.updateLeader(beerGame.getLeader());
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
					beerGames.add(new BeerGame(rs.getString(GAME_ID), rs.getString(GAME_NAME), rs.getString(GAME_DATE), rs.getString(GAME_END_DATE)));
				}
				conn.commit();
				conn.close();
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
				conn.close();
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
	public synchronized BeerGame getGameLog() {
		Connection conn = databaseConnection.connect();
		BeerGame beergame = null;
		beergame = getBeerGame(conn, beergame, READ_BEERGAME);
		return beergame;
	}

	/**
	 * A method which returns a list of ongoing beergame
	 * @return A beergame object
	 */
	public synchronized List<BeerGame> getGameLogFromOngoingGame() {
		Connection conn = databaseConnection.connect();
		return getBeerGames(conn, READ_ONGOING_BEERGAME);

	}

	/**
	 * A method which updates the GameEndDate of a game.
	 */
	public synchronized void updateEnddate(){
		Connection conn = databaseConnection.connect();
		if (conn != null) {
			try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_ENDDATE)) {

				conn.setAutoCommit(false);

				pstmt.setString(1, new Date().toString());
				pstmt.setString(2,DaoConfig.getCurrentGameId());

				pstmt.executeUpdate();
				conn.commit();
				conn.close();
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, e.toString(),e);
				databaseConnection.rollBackTransaction(conn);
			}
		}
	}

	/**
	 * A helper method which returns a beergame object
	 * @param conn
	 * @param beergame
	 * @param readOngoingBeergame
	 * @return
	 */
	private synchronized BeerGame getBeerGame(Connection conn, BeerGame beergame, String readOngoingBeergame) {
		if (conn != null) {
			try (PreparedStatement pstmt = conn.prepareStatement(readOngoingBeergame)) {
				conn.setAutoCommit(false);
				pstmt.setString(1, DaoConfig.getCurrentGameId());
				try (ResultSet rs = pstmt.executeQuery()) {
					if(!rs.isClosed()) {
						beergame = new BeerGame(rs.getString(GAME_ID), rs.getString(GAME_NAME), rs.getString(GAME_DATE), rs.getString(GAME_END_DATE));
						beergame.setConfiguration(configurationDAO.readConfiguration());
						beergame.setAgents(gameAgentDAO.readGameAgentsForABeerGame());
						beergame.setRounds(roundDAO.getRounds());
						beergame.setPlayers(playerDAO.getAllPlayers());
						beergame.setLeader(leaderDAO.getLeader());
					}
				}
				conn.commit();
				conn.close();
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, e.toString(),e);
			}
		}
		return beergame;
	}

	/**
	 * Helper function which returns a list of beergames
	 * @param conn
	 * @param readOngoingBeergame
	 * @return
	 */
	private synchronized List<BeerGame> getBeerGames(Connection conn, String readOngoingBeergame) {
		List<BeerGame> beergames = new ArrayList<>();
		if (conn != null) {
			try (PreparedStatement pstmt = conn.prepareStatement(readOngoingBeergame)) {
				executePrepareStatment(conn, beergames, pstmt);
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, e.toString(),e);
			}
		}
		return beergames;
	}

	/**
	 * Helper method to execute a prepared statement
	 * @param conn
	 * @param beergames
	 * @param pstmt
	 * @throws SQLException
	 */
	private synchronized void executePrepareStatment(Connection conn, List<BeerGame> beergames, PreparedStatement pstmt) throws SQLException {
		BeerGame beerGame;
		conn.setAutoCommit(false);
		try (ResultSet rs = pstmt.executeQuery()) {
			if(!rs.isClosed()) {
				while (rs.next()) {
					beerGame = new BeerGame(rs.getString(GAME_ID), rs.getString(GAME_NAME), rs.getString(GAME_DATE), rs.getString(GAME_END_DATE));
					DaoConfig.setCurrentGameId(beerGame.getGameId());
					beerGame.setConfiguration(configurationDAO.readConfiguration());
					beerGame.setAgents(gameAgentDAO.readGameAgentsForABeerGame());
					beerGame.setRounds(roundDAO.getRounds());
					beerGame.setPlayers(playerDAO.getAllPlayers());
					beergames.add(beerGame);
				}
			}
		}
		conn.commit();
		conn.close();
	}


}





