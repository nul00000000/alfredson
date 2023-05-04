package net.schedge.cards.database.schedge;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.schedge.cards.Logger;
import net.schedge.cards.database.AccountConstants;
import net.schedge.cards.database.AccountDatabase;

public class SchedgeDatabase extends AccountDatabase {
	
	private PreparedStatement getProfileStatement;
	private PreparedStatement getProfileUStatement;
	private PreparedStatement getTokenPairStatement;

	public SchedgeDatabase() {
		super(AccountConstants.SCHEDGE_URL, AccountConstants.SCHEDGE_USER, AccountConstants.SCHEDGE_PASS);
	}

	@Override
	public void initStatements() {
		getProfileStatement = createStatement("SELECT * FROM profiles WHERE id = ?");
		getProfileUStatement = createStatement("SELECT * FROM profiles WHERE username = ?");
	}
	
	public boolean validatePair(int id, int token) {
		try {
			getTokenPairStatement.setInt(1, id);
			getTokenPairStatement.setInt(2, token);
			ResultSet rs = getTokenPairStatement.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			Logger.error("Could not validate token pair (" + e.getErrorCode() + " - " + e.getSQLState() + ")");
			return false;
		}
	}

	public SchedgeProfile selectProfile(int id) {
		try {
			getProfileStatement.setInt(1, id);
			ResultSet rs = getProfileStatement.executeQuery();
			if (rs.next()) {
				return new SchedgeProfile(rs.getInt("id"), rs.getString("username"), rs.getString("displayName"));
			}
			return new SchedgeProfile(0, null, null);
		} catch (SQLException e) {
			Logger.error("Count not select profile, SQL Error Code: " + e.getErrorCode() + " " + e.getSQLState());
			return null;
		}
	}

	public SchedgeProfile selectProfile(String username) {
		try {
			getProfileUStatement.setString(1, username);
			ResultSet rs = getProfileUStatement.executeQuery();
			if (rs.next()) {
				return new SchedgeProfile(rs.getInt("id"), rs.getString("username"), rs.getString("displayName"));
			}
			return new SchedgeProfile(0, null, null);
		} catch (SQLException e) {
			Logger.error("Count not select profile, SQL Error Code: " + e.getErrorCode() + " " + e.getSQLState());
			return null;
		}
	}

}
