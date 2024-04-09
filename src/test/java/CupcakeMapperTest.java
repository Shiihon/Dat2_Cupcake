import app.entities.CupcakePart;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CupcakeMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

public class CupcakeMapperTest {

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "cupcake_development";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    private List<CupcakePart> expectedCupcakeBottoms;
    private List<CupcakePart> expectedCupcakeTops;

    @BeforeEach
    void setupTestData() throws SQLException {
        expectedCupcakeBottoms = List.of(
                new CupcakePart(
                        1,
                        "Chocolate",
                        5,
                        CupcakePart.Type.BOTTOM
                ),
                new CupcakePart(
                        2,
                        "Pistachio",
                        6,
                        CupcakePart.Type.BOTTOM
                ),
                new CupcakePart(
                        3,
                        "Almond",
                        7,
                        CupcakePart.Type.BOTTOM
                )
        );

        expectedCupcakeTops = List.of(
                new CupcakePart(
                        1,
                        "Blueberry",
                        5,
                        CupcakePart.Type.TOP
                ),
                new CupcakePart(
                        2,
                        "Strawberry",
                        6,
                        CupcakePart.Type.TOP
                ),
                new CupcakePart(
                        3,
                        "Rum/Raisin",
                        7,
                        CupcakePart.Type.TOP
                )
        );

        try (Connection connection = connectionPool.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.execute("DELETE FROM order_items");
                statement.execute("DELETE FROM orders");
                statement.execute("DELETE FROM users");
                statement.execute("DELETE FROM cupcake_bottoms");
                statement.execute("DELETE FROM cupcake_tops");

                statement.execute("SELECT setval('cupcake_bottoms_cupcake_bottom_id_seq', 1)");
                statement.execute("SELECT setval('cupcake_tops_cupcake_top_id_seq', 1)");

                String cupcakeBottomsSql = "INSERT INTO cupcake_bottoms (cupcake_bottom_id, cupcake_bottom_name, cupcake_bottom_price) VALUES" + expectedCupcakeBottoms.stream()
                        .map(cupcakePart -> String.format(
                                "(%d, '%s', %d)",
                                cupcakePart.getCupcakePartId(),
                                cupcakePart.getName(),
                                cupcakePart.getPrice()
                        ))
                        .collect(Collectors.joining(", "));

                statement.execute(cupcakeBottomsSql);
                statement.execute("SELECT setval('cupcake_bottoms_cupcake_bottom_id_seq', COALESCE((SELECT MAX(cupcake_bottom_id)+1 FROM cupcake_bottoms), 1), false)");

                String cupcakeTopsSql = "INSERT INTO cupcake_tops (cupcake_top_id, cupcake_top_name, cupcake_top_price) VALUES" + expectedCupcakeTops.stream()
                        .map(cupcakePart -> String.format(
                                "(%d, '%s', %d)",
                                cupcakePart.getCupcakePartId(),
                                cupcakePart.getName(),
                                cupcakePart.getPrice()
                        ))
                        .collect(Collectors.joining(", "));

                statement.execute(cupcakeTopsSql);
                statement.execute("SELECT setval('cupcake_tops_cupcake_top_id_seq', COALESCE((SELECT MAX(cupcake_top_id)+1 FROM cupcake_tops), 1), false)");
            }
        }
    }

    @Test
    void getCupcakeBottomsTest() throws DatabaseException {
        List<CupcakePart> bottoms = CupcakeMapper.getAllCupcakeBottoms(connectionPool);

        Assertions.assertFalse(bottoms.isEmpty(), "Bottoms should not be empty");
        Assertions.assertEquals(expectedCupcakeBottoms.size(), bottoms.size());
        Assertions.assertEquals(expectedCupcakeBottoms, bottoms);
    }

    @Test
    void getCupcakeTopsTest() throws DatabaseException {
        List<CupcakePart> tops = CupcakeMapper.getAllCupcakeTops(connectionPool);

        Assertions.assertFalse(tops.isEmpty(), "Tops should not be empty");
        Assertions.assertEquals(expectedCupcakeTops.size(), tops.size());
        Assertions.assertEquals(expectedCupcakeTops, tops);
    }

    @Test
    void getCupcakeBottomByIdTest() throws DatabaseException {
        CupcakePart actualCupcakePart = CupcakeMapper.getCupcakeBottomById(3, connectionPool);

        Assertions.assertEquals(expectedCupcakeBottoms.get(2), actualCupcakePart);
    }

    @Test
    void getCupcakeTopByIdTest() throws DatabaseException {
        CupcakePart actualCupcakePart = CupcakeMapper.getCupcakeTopById(3, connectionPool);

        Assertions.assertEquals(expectedCupcakeTops.get(2), actualCupcakePart);
    }
}