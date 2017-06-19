package com.udelblue.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import com.udelblue.util.DateUtil;

@Service
public class UserRepository {

	/*
	 * @Autowired private JdbcTemplate jdbcTemplate;
	 */

	final Logger log = LoggerFactory.getLogger(UserRepository.class);

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public String activeAccountFromEmail(String email) {
		String SQL = "UPDATE users  SET [enabled] = '1' WHERE email = :email";
		SqlParameterSource namedParameters = new MapSqlParameterSource("email", email);
		String returntype = "";
		try {
			returntype = namedParameterJdbcTemplate.queryForObject(SQL, namedParameters, String.class);
		} catch (Exception e) {
			log.error(e.toString());
		}
		return returntype;
	}

	public String createAccount(String email, String username, String password, String firstname, String lastname) {

		String SQL = "INSERT INTO users ([username],[password] ,[email] ,[firstname],[lastname],[createdon],[enabled]) VALUES( :username , :password, :email , :firstname , :lastname, :createdon , :enabled);";
		int returntype = 0;
		try {
			Map<String, String> namedParameters = new HashMap<String, String>();
			namedParameters.put("email", email);
			namedParameters.put("password", password);
			namedParameters.put("username", username);
			namedParameters.put("firstname", firstname);
			namedParameters.put("lastname", lastname);
			namedParameters.put("createdon", DateUtil.DateTimeNow());
			namedParameters.put("enabled", "0");
			returntype = namedParameterJdbcTemplate.update(SQL, namedParameters);
		} catch (Exception e) {
			log.error(e.toString());
		}
		String r = Integer.toString(returntype);
		return r;
	}

	public String createAuthorities(String username, List<String> authorities) {
		String r = "";
		for (String authority : authorities) {
			r = createAuthority(username, authority);
		}
		return r;
	}

	public String createAuthority(String username, String authority) {
		String SQL = "INSERT INTO [dbo].[authorities]([username],[authority]) VALUES (:username ,:authority);";
		int returntype = 0;

		try {
			Map<String, String> namedParameters = new HashMap<String, String>();
			namedParameters.put("username", username);
			namedParameters.put("authority", authority);
			returntype = namedParameterJdbcTemplate.update(SQL, namedParameters);
		} catch (Exception e) {
			log.error(e.toString());
		}
		String r = Integer.toString(returntype);
		return r;
	}

	public String deleteAccessToken(String username, String clientID) {
		String SQL = "DELETE FROM oauth_access_token where user_name  = :username and client_id = :clientid";
		int returntype = 0;
		try {
			Map<String, String> namedParameters = new HashMap<String, String>();
			namedParameters.put("username", username);
			namedParameters.put("clientid", clientID);
			namedParameterJdbcTemplate.update(SQL, namedParameters);

		} catch (Exception e) {
			log.error(e.toString());
		}
		String r = Integer.toString(returntype);
		return r;
	}

	public String deleteAllAccessTokens(String username) {
		String SQL = "DELETE FROM oauth_access_token where user_name  = :username";
		int returntype = 0;
		try {
			Map<String, String> namedParameters = new HashMap<String, String>();
			namedParameters.put("username", username);
			namedParameterJdbcTemplate.update(SQL, namedParameters);

		} catch (Exception e) {
			log.error(e.toString());
		}
		String r = Integer.toString(returntype);
		return r;
	}

	// checks if email in already registered
	public boolean emailExists(String email) {
		boolean exists = true;
		if (getUsernameFromEmail(email).equals("")) {
			exists = false;
		}
		;
		return exists;
	}

	public String getEmailFromID(String id) {

		String SQL = "SELECT email FROM users WHERE id = :id";
		SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
		String returntype = "";
		try {
			returntype = namedParameterJdbcTemplate.queryForObject(SQL, namedParameters, String.class);

		} catch (Exception e) {
			log.error(e.toString());
			returntype = "0";
		}

		return returntype;
	}

	public String getIDFromEmail(String email) {

		String SQL = "SELECT id FROM users WHERE email = :email";
		SqlParameterSource namedParameters = new MapSqlParameterSource("email", email);
		String returntype = "0";
		try {
			returntype = namedParameterJdbcTemplate.queryForObject(SQL, namedParameters, String.class);

		} catch (Exception e) {
			log.error(e.toString());
			returntype = "0";
		}

		return returntype;
	}

	public String getUsernameFromEmail(String email) {

		String SQL = "SELECT username FROM users WHERE email = :email";
		SqlParameterSource namedParameters = new MapSqlParameterSource("email", email);
		String returntype = "";
		try {
			returntype = namedParameterJdbcTemplate.queryForObject(SQL, namedParameters, String.class);

		} catch (Exception e) {
			log.error(e.toString());

		}

		return returntype;
	}

	public String loginAuditAccount(String username, String eventtime, String eventtype, String ipaddress,
			String operatingsystem, String browser, String uri, String fingerprint) {
		String SQL = "INSERT INTO user_audit_log  ([username],[eventtime] ,[eventtype]  ,[ipaddress] ,[operatingsystem],[device_name],[uri] , [browser_fingerprint]) VALUES( :username , :eventtime , :eventtype, :ipaddress, :operatingsystem , :device_name, :uri , :browser_fingerprint);";
		int returntype = 0;
		try {
			Map<String, String> namedParameters = new HashMap<String, String>();
			namedParameters.put("username", username);
			namedParameters.put("eventtime", eventtime);
			namedParameters.put("eventtype", eventtype);
			namedParameters.put("ipaddress", ipaddress);
			namedParameters.put("operatingsystem", operatingsystem);
			namedParameters.put("device_name", browser);
			namedParameters.put("uri", uri);
			namedParameters.put("browser_fingerprint", fingerprint);
			returntype = namedParameterJdbcTemplate.update(SQL, namedParameters);
		} catch (Exception e) {
			log.error(e.toString());
		}
		String r = Integer.toString(returntype);
		return r;
	}

	public String setAccountPasswordFromEmail(String email, String password) {
		String SQL = "UPDATE users  SET [password] = :password   WHERE email = :email";
		int returntype = 0;
		try {
			Map<String, String> namedParameters = new HashMap<String, String>();
			namedParameters.put("email", email);
			namedParameters.put("password", password);
			namedParameterJdbcTemplate.update(SQL, namedParameters);

		} catch (Exception e) {
			log.error(e.toString());
		}
		String r = Integer.toString(returntype);
		return r;
	}

	public String setLastLoginFromUsername(String username) {
		String SQL = "UPDATE users  SET [lastlogin] = :now   WHERE username = :username";
		int returntype = 0;
		try {
			Map<String, String> namedParameters = new HashMap<String, String>();
			namedParameters.put("username", username);
			namedParameters.put("now", DateUtil.DateTimeNow());
			namedParameterJdbcTemplate.update(SQL, namedParameters);

		} catch (Exception e) {
			log.error(e.toString());
		}
		String r = Integer.toString(returntype);
		return r;
	}

	public boolean usernameExists(String username) {
		boolean exists = true;
		String SQL = "SELECT username FROM users WHERE username = :username";
		SqlParameterSource namedParameters = new MapSqlParameterSource("username", username);
		String returntype = "";
		try {
			returntype = namedParameterJdbcTemplate.queryForObject(SQL, namedParameters, String.class);

		} catch (Exception e) {
			log.error(e.toString());
			returntype = "0";
		}
		if (returntype.equals("0")) {
			exists = false;
		}

		return exists;

	}

}
