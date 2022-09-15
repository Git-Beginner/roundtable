package com.roundtable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

	private String GET_SQL = "SELECT * FROM USER WHERE USERNAME = :username";
	private String INSERT_SQL = "INSERT INTO USER VALUES (?, ?, ?, ?, ? , ?)";

	@Autowired
	private NamedParameterJdbcTemplate npJdbcTemplate;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public User getUser(String username) {
		
		jdbcTemplate.execute("ALTER SESSION SET JDBC_QUERY_RESULT_FORMAT='JSON'");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("username", username);
		List<User> users = npJdbcTemplate.query(GET_SQL, namedParameters, new RowMapper<User>() {

			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {

				User user = new User();
				user.setAddress(rs.getString("ADDRESS"));
				user.setUsername(rs.getString("USERNAME"));
				user.setEmailId(rs.getString("EMAILID"));
				user.setId(rs.getString("ID"));
				user.setPhone(rs.getString("PHONE"));
				user.setPassword(rs.getString("PASSWORD"));
				return user;
			}

		});
		return users == null || users.isEmpty() ? new User() : users.get(0);
	}

	public User saveUser(User user) {
		user.setId(user.getUsername() + "@" + user.getPhone());
		jdbcTemplate.batchUpdate(INSERT_SQL, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, user.getId());
				ps.setString(2, user.getUsername());
				ps.setString(3, user.getEmailId());
				ps.setString(4, user.getPhone());
				ps.setString(5, user.getAddress());
				ps.setString(6, user.getPassword());
			}

			@Override
			public int getBatchSize() {
				return 1;
			}
		});
		return user;
	}

}
