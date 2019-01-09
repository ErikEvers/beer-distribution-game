package org.han.ica.asd.c.dao;

import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.exception.GameIdNotSetException;
import org.han.ica.asd.c.model.domain_objects.Leader;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LeaderDAO {
	private static final String CREATE_LEADER = "INSERT INTO Leader VALUES (?,?,?);";
	private static final Logger LOGGER = Logger.getLogger(LeaderDAO.class.getName());

	@Inject
	IDatabaseConnection databaseConnection;

	public void insertLeader(Leader leader) {
		Connection conn = databaseConnection.connect();
		if (conn != null) {
			try (PreparedStatement pstmt = conn.prepareStatement(CREATE_LEADER)) {
				conn.setAutoCommit(false);

				pstmt.setString(1, DaoConfig.getCurrentGameId());
				pstmt.setString(2,leader.getPlayer().getPlayerId());
				pstmt.setString(3, new Date().toString());

				pstmt.executeUpdate();
				conn.commit();
			} catch (GameIdNotSetException | SQLException e) {
				LOGGER.log(Level.SEVERE, e.toString(), e);
				databaseConnection.rollBackTransaction(conn);
			}
		}
	}
}

