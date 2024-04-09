import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import org.junit.jupiter.api.Assertions;
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
                        "jesper@cphbusiness.com",
                        "1234",
                        "customer",
                        500
                ),
                new User(
                        3,
                        "bob@gmail.com",
                        "1234",
                        "customer",
                        500
                )
        );

        try (Connection connection = connectionPool.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.execute("DELETE FROM order_items");
                statement.execute("DELETE FROM orders");
                statement.execute("DELETE FROM users");
                statement.execute("DELETE FROM cupcake_bottoms");
                statement.execute("DELETE FROM cupcake_tops");

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
    void loginUserTest() throws DatabaseException {
        User expectedUser = expectedUsers.get(0);
        User actualUser = UserMapper.login(expectedUser.getEmail(), expectedUser.getPassword(), connectionPool);

        Assertions.assertEquals(expectedUser, actualUser);
    }

    @Test
    void getUserByIdTest() throws DatabaseException {
        User expectedUser = expectedUsers.get(0);
        User actualUser = UserMapper.getUserById(expectedUser.getUserId(), connectionPool);

        Assertions.assertEquals(expectedUser, actualUser);
    }

    @Test
    void createUserTest() throws DatabaseException {
        User expectedUser = new User(expectedUsers.size() + 1, "Nanna@cphbusiness.com", "1234", "customer", 500);
        UserMapper.createAccount(expectedUser.getEmail(), expectedUser.getPassword(), connectionPool);
        User actualUser = UserMapper.getUserById(expectedUser.getUserId(), connectionPool);

        Assertions.assertEquals(expectedUser, actualUser);
    }

    @Test
    void getAllUsersByRoleTest() throws DatabaseException {
        List<User> expectedCustomerUsers = List.of(
                expectedUsers.get(1),
                expectedUsers.get(2)
        );

        List<User> actualCustomerUsers = UserMapper.getAllUsersByRole("customer", connectionPool);
        Assertions.assertEquals(expectedCustomerUsers.size(), actualCustomerUsers.size());
        Assertions.assertEquals(expectedCustomerUsers, actualCustomerUsers);
    }
}