package com.group.libraryapp.repository.user;

import com.group.libraryapp.dto.user.response.UserResponse;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createUser(String name, Integer age) {
        String sql = "INSERT INTO library.user (name, age) VALUES (? , ?)";
        jdbcTemplate.update(sql, name, age);
    }

    public List<UserResponse> getUserList() {
        String sql = "SELECT * FROM library.user";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            int age = rs.getInt("age");
            return new UserResponse(id, name, age);
        });
    }

    public boolean isUserNotExist(long id) {
        String readSql = "SELECT * FROM library.user WHERE id = ?";
        return jdbcTemplate.query(readSql, (rs, rowNum) -> 0, id).isEmpty();
    }

    public void updateUser(String name, long id) {
        String sql = "UPDATE library.user SET name = ? where id = ?";
        jdbcTemplate.update(sql, name, id);
    }

    public boolean isNotUserExist(String name) {
        String readSql = "SELECT * FROM library.user WHERE name = ?";
        return jdbcTemplate.query(readSql, (rs, rowNum) -> 0, name).isEmpty();
    }

    public void deleteUser(String name) {
        String sql = "DELETE FROM library.user WHERE name = ?";
        jdbcTemplate.update(sql, name);
    }
}
