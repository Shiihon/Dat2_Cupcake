import app.persistence.ConnectionPool;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CupcakeMapperTest {

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "cupcake_development";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    @BeforeEach
    void setupTestData() throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement statement = connection.createStatement()) {

                statement.execute("TRUNCATE TABLE cupcake_bottoms RESTART IDENTITY CASCADE");
                statement.execute("TRUNCATE TABLE cupcake_tops RESTART IDENTITY CASCADE");

                statement.execute("INSERT INTO cupcake_bottoms (cupcake_bottom_name, cupcake_bottom_price) VALUES ('Chocolate', 5), ('Vanilla', 4)");

                statement.execute("INSERT INTO cupcake_tops (cupcake_top_name, cupcake_top_price) VALUES ('Blueberry', 5), ('Strawberry', 6)");
            }
        }
    }

    @Test
    void getCupcakeBottomsTest() throws DatabaseException {
        List<CupcakePart> bottoms = CupcakeMapper.getCupcakeBottoms(connectionPool);
        assertFalse(bottoms.isEmpty(), "Bottoms list should not be empty");
        assertTrue(bottoms.stream().anyMatch(b -> b.getName().equals("Chocolate") && b.getPrice() == 5), "List should contain Chocolate bottom with price 5");
    }

    @Test
    void getCupcakeTopsTest() throws DatabaseException {
        List<CupcakePart> tops = CupcakeMapper.getCupcakeTops(connectionPool);
        assertFalse(tops.isEmpty(), "Tops list should not be empty");
        assertTrue(tops.stream().anyMatch(t -> t.getName().equals("Blueberry") && t.getPrice() == 5), "List should contain Blueberry top with price 5");
    }

    @Test
    void getCupcakePartNameAndPriceTest() throws DatabaseException {
        // Assuming the first inserted bottom has ID 1
        String bottomName = CupcakeMapper.getCupcakePartName(1, connectionPool, CupcakePart.Type.BOTTOM);
        int bottomPrice = CupcakeMapper.getCupcakePartPrice(1, connectionPool, CupcakePart.Type.BOTTOM);

        assertEquals("Chocolate", bottomName, "Name should match Chocolate");
        assertEquals(5, bottomPrice, "Price should be 5 for Chocolate bottom");
    }
}