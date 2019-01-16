package org.han.ica.asd.c.dao;

import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.domain_objects.Leader;
import org.han.ica.asd.c.model.domain_objects.Player;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LeaderDAO {
	private static final String CREATE_LEADER = "INSERT INTO Leader VALUES (?,?,?);";
	private static final String GET_LEADER = "SELECT * FROM Leader WHERE GameId = ? ORDER BY Timestamp DESC LIMIT 1;";
	private static final Logger LOGGER = Logger.getLogger(LeaderDAO.class.getName());
	private static final String UPDATE_LEADER = "UPDATE Leader SET PlayerId = ? AND TIMESTAMP = ? WHERE GameId = ?;";

	@Inject
	IDatabaseConnection databaseConnection;

	@Inject
	PlayerDAO playerDAO;

	public void insertLeader(Leader leader) {
		Connection conn = databaseConnection.connect();
		if (conn != null) {
			try (PreparedStatement pstmt = conn.prepareStatement(CREATE_LEADER)) {
				conn.setAutoCommit(false);

				pstmt.setString(1, DaoConfig.getCurrentGameId());
				pstmt.setString(2, leader.getPlayer().getPlayerId());
				if(leader.getTimestamp() != "") {
					pstmt.setString(3, leader.getTimestamp());
				} else {
					pstmt.setString(3, new Date().toString());
				}

				pstmt.executeUpdate();
				conn.commit();
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, e.toString(), e);
				databaseConnection.rollBackTransaction(conn);
			}
		}
	}

	public Leader getLeader() {
		Leader leader = null;
		Connection conn = databaseConnection.connect();

		if (conn == null) {
			return leader;
		}
		try (PreparedStatement pstmt = conn.prepareStatement(GET_LEADER)) {
			conn.setAutoCommit(false);
			pstmt.setString(1, DaoConfig.getCurrentGameId());
			try (ResultSet rs = pstmt.executeQuery()) {
				if (!rs.isClosed()) {
					rs.next();
					leader = new Leader(playerDAO.getPlayer(rs.getString("PlayerId")));
					leader.setTimestamp(rs.getString("Timestamp"));
				}
			}
			conn.commit();
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			databaseConnection.rollBackTransaction(conn);
		}

		return leader;
	}

	public void updateLeader(Leader leader) {
		Connection conn = databaseConnection.connect();
		if (conn != null) {
			try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_LEADER)) {
				conn.setAutoCommit(false);

				pstmt.setString(1, leader.getPlayer().getPlayerId());
				if(leader.getTimestamp() != "") {
					pstmt.setString(2, leader.getTimestamp());
				} else {
					pstmt.setString(2, new Date().toString());
				}
				pstmt.setString(3, DaoConfig.getCurrentGameId());

				pstmt.executeUpdate();
				conn.commit();
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, e.toString(), e);
				databaseConnection.rollBackTransaction(conn);
			}
		}
	}
}


