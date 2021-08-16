package db.migration;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.web.vexamine.utils.PasswordUtils;
import org.web.vexamine.utils.VexamineConstants;

public class V1_201906052049__AdminLoginMigration extends BaseJavaMigration {

	/*
	 * static final String superAdminUser = "admin"; static final String
	 * superAdminPass = "V5x@m1n3";
	 */

	static final String vexamineAdmin = "vexamineAdmin";

	static final String vexamineManager = "vexamineManager";

	static final String vexamineCandidate = "vexamineCandidate";

	static final String insertCredSql = "INSERT INTO vexamine_user_credentials(mail_id, user_name, hashed_salt, hashed_password,"
	        + " create_user, create_date, invite_sent) VALUES (?,?,?,?,?,?,?)";

	static final String selectCredSql = "Select id from vexamine_user_credentials where mail_id= ?";

	static final String selectRoleSql = "Select id from vexamine_role where type= ? ";
	
	static final String insertAuthSql = "INSERT INTO vexamine_user_authority_info(user_id, user_role_id, create_user, update_user) VALUES (?,?,?,?)";
	
	@Override
	public void migrate(Context context) throws Exception {
		Connection connObject = context.getConnection();

		insertVexamineUser(connObject, vexamineAdmin, vexamineAdmin, VexamineConstants.SUPER_ADMIN_ROLE);
		authorizeVexamineUser(connObject, vexamineAdmin, VexamineConstants.SUPER_ADMIN_ROLE);

		insertVexamineUser(connObject, vexamineManager, vexamineManager, VexamineConstants.MANAGER_ROLE);
		authorizeVexamineUser(connObject, vexamineManager, VexamineConstants.MANAGER_ROLE);

		insertVexamineUser(connObject, vexamineCandidate, vexamineCandidate, VexamineConstants.CANDIDATE_ROLE);
		authorizeVexamineUser(connObject, vexamineCandidate, VexamineConstants.CANDIDATE_ROLE);
	}

	private void insertVexamineUser(Connection connObject, String userName, String password, String roleType) {
		try {
			PreparedStatement prepareStmt = connObject.prepareStatement(insertCredSql);
			prepareStmt.setString(1, userName);
			prepareStmt.setString(2, userName);

			String secureSalt = PasswordUtils.generateSALT(30);
			prepareStmt.setBytes(3, secureSalt.getBytes());

			byte[] hashedPassword = PasswordUtils.generateSecurePassword(password, secureSalt);
			prepareStmt.setBytes(4, hashedPassword);

			prepareStmt.setString(5, VexamineConstants.SYSTEM_USER);
			Date currDate = new Date();
			prepareStmt.setTimestamp(6, new Timestamp(currDate.getTime()));

			prepareStmt.setBoolean(7, VexamineConstants.INVITE_SENT_DEFAULT);

			prepareStmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void authorizeVexamineUser(Connection connObject, String userName, String roleType) {
		try {
			PreparedStatement prepareCredStmt = connObject.prepareStatement(selectCredSql);
			prepareCredStmt.setString(1, userName);
			ResultSet userCred = prepareCredStmt.executeQuery();

			PreparedStatement prepareRoleStmt = connObject.prepareStatement(selectRoleSql);
			prepareRoleStmt.setString(1, roleType);
			ResultSet userAuth = prepareRoleStmt.executeQuery();

			while (userCred.next() && userAuth.next()) {
				int userCredId = userCred.getInt(1);
				int userAuthId = userAuth.getInt(1);
				insertVexamineUserAuthority(connObject, userCredId, userAuthId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void insertVexamineUserAuthority(Connection connObject, int userCredId, int userAuthId) {
		PreparedStatement prepareAuthStmt;
		try {
			prepareAuthStmt = connObject.prepareStatement(insertAuthSql);
			prepareAuthStmt.setInt(1, userCredId);
			prepareAuthStmt.setInt(2, userAuthId);
			prepareAuthStmt.setString(3, VexamineConstants.SYSTEM_USER);
			prepareAuthStmt.setString(4, VexamineConstants.SYSTEM_USER);
			prepareAuthStmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
