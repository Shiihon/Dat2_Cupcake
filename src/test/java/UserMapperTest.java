import app.entities.User;
import app.persistence.ConnectionPool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

public class UserMapperTest {

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "cupcake_development";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    private List<User> expectedUsers;

    @BeforeEach
    void setupTestData() throws SQLException {
        expectedUsers = List.of(
                new User(
                        1,
                        "admin@bussiness.com",
                        "1234",
                        "admin",
                        0
                ),
                new User(
                        2,
                        "bob@gmail.com",
                        "1234",
                        "customer",
                        500
                )
        );

        try (Connection connection = connectionPool.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.execute("DELETE FROM users");

                statement.execute("SELECT setval('users_user_id_seq', 1)");

                String userSql = "INSERT INTO users (user_id, user_email, user_password, user_role, user_balance) VALUES" + expectedUsers.stream()
                        .map(user -> String.format(
                                "(%d, '%s', '%s', '%s', %d)",
                                user.getUserId(),
                                user.getEmail(),
                                user.getPassword(),
                                user.getRole(),
                                user.getBalance()
                        ))
                        .collect(Collectors.joining(", "));

                statement.execute(userSql);
                statement.execute("SELECT setval('users_user_id_seq', COALESCE((SELECT MAX(user_id)+1 FROM users), 1), false)");
            }
        }
    }

    @Test
    void loginUserTest() {

    }

    @Test
    void createUserTest() {

    }
}