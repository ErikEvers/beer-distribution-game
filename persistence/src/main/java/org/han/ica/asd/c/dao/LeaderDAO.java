package org.han.ica.asd.c.dao;

import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.domain_objects.Leader;
import org.han.ica.asd.c.model.domain_objects.Player;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LeaderDAO {
	private static final String CREATE_LEADER = "INSERT INTO Leader VALUES (?,?,?);";
	private static final String GET_LEADER = "SELECT * FROM Leader WHERE GameId = ? ORDER BY Timestamp DESC LIMIT 1;";
	private static final Logger LOGGER = Logger.getLogger(LeaderDAO.class.getName());

	@Inject
	IDatabaseConnection databaseConnection;

	@Inject
	PlayerDAO playerDAO;

	public void insertLeader(Player player) {
		Connection conn = databaseConnection.connect();
		if (conn != null) {
			try (PreparedStatement pstmt = conn.prepareStatement(CREATE_LEADER)) {
				conn.setAutoCommit(false);

				pstmt.setString(1, DaoConfig.getCurrentGameId());
				pstmt.setString(2, player.getPlayerId());
				pstmt.setString(3, new Date().toString());

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
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
					leader.setTimestamp(LocalDateTime.parse(rs.getString("Timestamp"), formatter));
				}
			}
			conn.commit();
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			databaseConnection.rollBackTransaction(conn);
		}

		return leader;
	}

}


