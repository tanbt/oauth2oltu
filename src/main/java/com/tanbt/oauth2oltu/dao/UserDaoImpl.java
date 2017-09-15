package com.tanbt.oauth2oltu.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.tanbt.oauth2oltu.entity.Login;
import com.tanbt.oauth2oltu.entity.User;

public class UserDaoImpl implements UserDao {
    @Autowired
    DataSource datasource;
    @Autowired
    JdbcTemplate jdbcTemplate;
    public void register(User user) {
        String sql = "insert into users values(?,?,?,?,?)";
        jdbcTemplate.update(sql,
                new Object[] { user.getEmail(), user.getPassword(),
                        user.getFirstname(), user.getLastname(),
                        user.getOrganization() });
    }

    public User validateUser(Login login) {
        String sql =
                "select * from users where email='" + login.getEmail() +
                        "' and password='" + login.getPassword() + "'";
        List<User> users = jdbcTemplate.query(sql, new UserMapper());
        return users.size() > 0 ? users.get(0) : null;
    }
}
class UserMapper implements RowMapper<User> {
    public User mapRow(ResultSet rs, int arg1) throws SQLException {
        User user = new User();
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setFirstname(rs.getString("firstname"));
        user.setLastname(rs.getString("lastname"));
        user.setOrganization(rs.getInt("organization"));
        return user;
    }
}
