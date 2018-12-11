package org.han.ica.asd.c;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.han.ica.asd.c.dbconnection.DBConnection.RollBackTransaction;
import static org.han.ica.asd.c.dbconnection.DBConnection.connect;

public class RoundDAO {
	private static final String CREATE_ROUND = "INSERT INTO ROUND VALUES(?,?)";
	private static final String READ_ROUND = "";
	private static final String UPDATE_ROUND = "";
	private static final String DELETE_ROUND = "";


	public void createRound(String gameId, int roundId){
		Connection conn = null;
		try {
			conn = connect();
			if (conn == null) return;
			try (PreparedStatement pstmt = conn.prepareStatement(CREATE_ROUND)) {

				conn.setAutoCommit(false);

				pstmt.setString(1, gameId);
				pstmt.setInt(2, roundId);

				pstmt.executeUpdate();
			}
			conn.commit();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			RollBackTransaction(conn);
		}
	}
}
