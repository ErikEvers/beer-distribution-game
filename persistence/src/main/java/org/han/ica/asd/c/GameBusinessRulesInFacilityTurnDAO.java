package org.han.ica.asd.c;

import org.han.ica.asd.c.model.GameBusinessRulesInFacilityTurn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.han.ica.asd.c.dbconnection.DBConnection.connect;

public class GameBusinessRulesInFacilityTurnDAO implements IBeerDisitributionGameDAO {
	public static final String CREATE_BUSINESSRULETURN = "INSERT INTO GameBusinessRuleInFacility VALUES (?,?,?,?,?,?,?);";
	private static final Logger LOGGER = Logger.getLogger(GameBusinessRulesInFacilityTurnDAO.class.getName());

	public void createTurn(GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn) {
		Connection conn;
		try {
			conn = connect();
			try (PreparedStatement pstmt = conn.prepareStatement(CREATE_BUSINESSRULETURN)) {
				pstmt.setInt(1,gameBusinessRulesInFacilityTurn.getRoundId());
				pstmt.setInt(2,gameBusinessRulesInFacilityTurn.getFacilityOrder());
				pstmt.setInt(3,gameBusinessRulesInFacilityTurn.getFacilityDeliver());
				pstmt.setString(4,gameBusinessRulesInFacilityTurn.getGameId());
				pstmt.setString(5,gameBusinessRulesInFacilityTurn.getGameAgentName());
				pstmt.setString(6,gameBusinessRulesInFacilityTurn.getGameAST());

				pstmt.executeUpdate();
			}
			conn.commit();
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}
}
