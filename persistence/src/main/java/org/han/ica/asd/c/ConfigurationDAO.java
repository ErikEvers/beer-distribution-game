package org.han.ica.asd.c;

import org.han.ica.asd.c.model.Configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.han.ica.asd.c.dbconnection.DBConnection.RollBackTransaction;
import static org.han.ica.asd.c.dbconnection.DBConnection.connect;

public class ConfigurationDAO {
	//CRUD - Create, read, update, delete
	private static final String CREATE_CONFIGURATION = "INSERT INTO Configuration VALUES (?,?,?,?,?,?,?,?,?,?,?);";
	private static final String READ_CONFIGURATION = "SELECT* FROM Configuration;";
	private static final String UPDATE_CONFIGURATION = "";
	private static final String DELETE_CONFIGURATION = "";

	public void createConfiguration(String GameName, Date GameEndDate, int AmountOfRounds, int AmountOfFactories, int AmountOfWholesales, int AmountOfDistributors, int AmountOfRetailers, int MinimalOrderRetail, int MaximumOrderRetail, boolean ContinuePlayingWhenBankrupt, boolean InsightFacilities){
		Connection conn = null;
		try {
			conn = connect();
			if (conn == null) return;
			try (PreparedStatement pstmt = conn.prepareStatement(CREATE_CONFIGURATION)) {

				conn.setAutoCommit(false);

				pstmt.setString(1, GameName);
				pstmt.setString(2, new Date().toString());
				pstmt.setString(3, GameEndDate.toString());
				pstmt.setInt(3,AmountOfRounds);
				pstmt.setInt(4,AmountOfFactories);
				pstmt.setInt(5,AmountOfWholesales);
				pstmt.setInt(6,AmountOfDistributors);
				pstmt.setInt(7,AmountOfRetailers);
				pstmt.setInt(8,MinimalOrderRetail);
				pstmt.setInt(9,MaximumOrderRetail);
				pstmt.setBoolean(10,ContinuePlayingWhenBankrupt);
				pstmt.setBoolean(11,InsightFacilities);

				pstmt.executeUpdate();
			}
			conn.commit();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			RollBackTransaction(conn);
		}
	}

	public List<Configuration> readConfigurations(){
		Connection conn = null;
		ArrayList<Configuration> configurations = new ArrayList<>();
		try {
			conn = connect();
			if (conn == null) return null;
			try (PreparedStatement pstmt = conn.prepareStatement(READ_CONFIGURATION)) {

				try (ResultSet rs = pstmt.executeQuery()) {
					while (rs.next()) {
						configurations.add(new Configuration(rs.getString("GameId"), rs.getInt("AmountOfRounds"),
								rs.getInt("AmoundOfFactories"), rs.getInt("AmountOfWholesales"),
								rs.getInt("AmountOfDistributors"),rs.getInt("AmountOfRetailers"),
								rs.getInt("MinimalOrderRetail"), rs.getInt("MaximalOrderRetail"),
								rs.getBoolean("ContinuePlayingWhileBankrupt"), rs.getBoolean("InsightFacilities")));
					}
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return configurations;
	}

}
