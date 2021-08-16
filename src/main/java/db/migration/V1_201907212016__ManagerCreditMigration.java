package db.migration;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.web.vexamine.utils.VexamineConstants;

public class V1_201907212016__ManagerCreditMigration extends BaseJavaMigration {

	static final String vexamineAdmin = "vexamineAdmin";
	static final String vexamineManager = "vexamineManager";
	static final String vexamineCandidate = "vexamineCandidate";

	static final String selectAuthSql = "select * from vexamine_user_authority_info where user_id = (Select id from vexamine_user_credentials where mail_id= ? )";

	static final String selectCredSql = "Select id from vexamine_user_credentials where mail_id= ?";

	static final String selectManagerCredSql = "Select id from vexamine_manager_credit where manager_auth_id = ?";

	static final String insertManagerCreditSql = "INSERT INTO vexamine_manager_credit(manager_auth_id, create_user, create_date) VALUES (?,?,?)";

	static final String insertManagerCandidateSql = "INSERT INTO vexamine_manager_credit_candidate_list(manager_credit_id, candidate_list_id) VALUES (?,?)";

	static final String updateManagerCreditSql = "UPDATE vexamine_manager_credit SET candidate_list=?, update_user=? WHERE id=?";

	@Override
	public void migrate(Context context) throws Exception {
		Connection connObject = context.getConnection();
		addVexamineManagerCredits(connObject, vexamineManager);
	}

	private void addVexamineManagerCredits(Connection connObject, String managerUser) {
		try {
			PreparedStatement prepareCredStmt = connObject.prepareStatement(selectAuthSql);
			prepareCredStmt.setString(1, managerUser);
			ResultSet userAuth = prepareCredStmt.executeQuery();

			while (userAuth.next()) {
				int managerAuthId = userAuth.getInt(1);
				insertManagerCredit(connObject, managerAuthId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void insertManagerCredit(Connection connObject, int managerAuthId) throws SQLException {
		PreparedStatement prepareCredStmt = connObject.prepareStatement(selectCredSql);
		prepareCredStmt.setString(1, vexamineManager);
		ResultSet userCred = prepareCredStmt.executeQuery();

		try {
			while (userCred.next()) {
				PreparedStatement prepareAuthStmt = connObject.prepareStatement(insertManagerCreditSql);
				prepareAuthStmt.setInt(1, managerAuthId);
				prepareAuthStmt.setString(2, VexamineConstants.SYSTEM_USER);
				Date currDate = new Date();
				prepareAuthStmt.setTimestamp(3, new Timestamp(currDate.getTime()));
				prepareAuthStmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		insertManagerCandidate(connObject, managerAuthId);
	}

	private void insertManagerCandidate(Connection connObject, int managerAuthId) throws SQLException {
		PreparedStatement prepareCredStmt = connObject.prepareStatement(selectManagerCredSql);
		prepareCredStmt.setInt(1, managerAuthId);
		ResultSet managerAuth = prepareCredStmt.executeQuery();
		
		PreparedStatement prepareCandidStmt = connObject.prepareStatement(selectAuthSql);
		prepareCandidStmt.setString(1, vexamineCandidate);
		ResultSet candidateAuth = prepareCandidStmt.executeQuery();

		PreparedStatement prepareAuthStmt;
		try {
			while (managerAuth.next() && candidateAuth.next()) {
				prepareAuthStmt = connObject.prepareStatement(insertManagerCandidateSql);
				prepareAuthStmt.setInt(1, managerAuth.getInt(1));
				prepareAuthStmt.setInt(2, candidateAuth.getInt(1));
				prepareAuthStmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
