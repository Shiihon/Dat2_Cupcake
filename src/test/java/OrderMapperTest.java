import app.entities.CupcakePart;
import app.entities.Order;
import app.entities.OrderItem;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapperTest {

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "cupcake_development";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    private List<CupcakePart> expectedCupcakeBottoms;
    private List<CupcakePart> expectedCupcakeTops;
    private List<OrderItem> expectedOrderItems;
    private List<Order> expectedOrders;
    private List<User> expectedUsers;

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


        expectedOrderItems = List.of(
                new OrderItem(
                        1,
                        1,
                        expectedCupcakeTops.get(0),
                        expectedCupcakeBottoms.get(0),
                        4
                ),
                new OrderItem(
                        2,
                        1,
                        expectedCupcakeTops.get(1),
                        expectedCupcakeBottoms.get(1),
                        10
                ),

                new OrderItem(
                        3,
                        2,
                        expectedCupcakeTops.get(2),
                        expectedCupcakeBottoms.get(2),
                        6
                )
        );


        expectedOrders = List.of(
                new Order(
                        1,
                        1,
                        List.of(
                                expectedOrderItems.get(0),
                                expectedOrderItems.get(1)
                        ),
                        "In Progress",
                        LocalDateTime.parse("2024-04-01T10:49:21")
                ),
                new Order(
                        2,
                        2,
                        List.of(
                                expectedOrderItems.get(2)
                        ),
                        "Complete",
                        LocalDateTime.parse("2024-03-28T14:52:33")
                )
        );


        expectedUsers = List.of(
                new User(
                        1,
                        "bob@gmail.com",
                        "1234",
                        "customer",
                        500
                ),
                new User(
                        2,
                        "jon@cphbusiness.dk",
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

                statement.execute("SELECT setval('cupcake_bottoms_cupcake_bottom_id_seq', 1)");
                statement.execute("SELECT setval('cupcake_tops_cupcake_top_id_seq', 1)");
                statement.execute("SELECT setval('order_items_order_item_id_seq', 1)");
                statement.execute("SELECT setval('orders_order_id_seq', 1)");
                statement.execute("SELECT setval('users_user_id_seq', 1)");


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


                String usersSql = "INSERT INTO users (user_id, user_email, user_password, user_role, user_balance) VALUES" + expectedUsers.stream()
                        .map(user -> String.format(
                                "(%d, '%s', '%s', '%s', %d)",
                                user.getUserId(),
                                user.getEmail(),
                                user.getPassword(),
                                user.getRole(),
                                user.getBalance()
                        ))
                        .collect(Collectors.joining(", "));

                statement.execute(usersSql);
                statement.execute("SELECT setval('users_user_id_seq', COALESCE((SELECT MAX(user_id)+1 FROM users), 1), false)");


                String ordersSql = "INSERT INTO orders (order_id, user_id, order_status, order_timestamp) VALUES" + expectedOrders.stream()
                        .map(order -> String.format(
                                "(%d, %d, '%s', '%s')",
                                order.getOrderId(),
                                order.getUserId(),
                                order.getStatus(),
                                order.getTimestamp()
                        ))
                        .collect(Collectors.joining(", "));

                statement.execute(ordersSql);
                statement.execute("SELECT setval('orders_order_id_seq', COALESCE((SELECT MAX(order_id)+1 FROM orders), 1), false)");


                String orderItemsSql = "INSERT INTO order_items (order_item_id, order_id, cupcake_bottom_id, cupcake_top_id, order_item_quantity) VALUES" + expectedOrderItems.stream()
                        .map(orderItem -> String.format(
                                "(%d, %d, %d, %d, %d)",
                                orderItem.getOrderItemId(),
                                orderItem.getOrderId(),
                                orderItem.getCupcakeBottom().getCupcakePartId(),
                                orderItem.getCupcakeTop().getCupcakePartId(),
                                orderItem.getQuantity()
                        ))
                        .collect(Collectors.joining(", "));

                statement.execute(orderItemsSql);
                statement.execute("SELECT setval('order_items_order_item_id_seq', COALESCE((SELECT MAX(order_item_id)+1 FROM order_items), 1), false)");
            }
        }
    }

    @Test
    void getOrderById() throws DatabaseException {
        Order actualOrder = OrderMapper.getOrderById(2, connectionPool);
        Assertions.assertEquals(expectedOrders.get(1), actualOrder);
    }

    @Test
    void getAllOrdersTest() throws DatabaseException{
        List<Order> actualOrders = OrderMapper.getAllOrders(connectionPool);

        Assertions.assertEquals(expectedOrders.size(), actualOrders.size());
        Assertions.assertEquals(expectedOrders, actualOrders);
    }

    @Test
    void getAllOrdersByStatusTest() throws DatabaseException {
        List<Order> actualCompleteOrders = OrderMapper.getAllOrdersByStatus("complete", connectionPool);

        Assertions.assertEquals(1, actualCompleteOrders.size());
        Assertions.assertEquals(expectedOrders.get(1), actualCompleteOrders.get(0));
    }

    @Test
    void getAllUserOrdersTest() throws DatabaseException {
        List<Order> actualUserOrders = OrderMapper.getAllUserOrders(1, connectionPool);

        Assertions.assertEquals(2, actualUserOrders.size());
        Assertions.assertEquals(List.of(
                expectedOrders.get(0),
                expectedOrders.get(1)
        ), actualUserOrders);
    }

    @Test
    void createOrderTest() throws DatabaseException {
        Order expectedOrder = new Order(
                3,
                2,
                List.of(
                        new OrderItem(
                                0,
                                3,
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
                                8
                        )
                ),
                "In Progress",
                LocalDateTime.now()
        );

        OrderMapper.createOrder(expectedOrder, connectionPool);

        List<Order> actualOrders = OrderMapper.getAllOrders(connectionPool);
        Assertions.assertEquals(expectedOrders.size() + 1, actualOrders.size());

        Order actualOrder = OrderMapper.getOrderById(expectedOrders.size(), connectionPool);
        Assertions.assertEquals(expectedOrder, actualOrder);
    }

    @Test
    void deleteOrderTest() throws DatabaseException {
        OrderMapper.deleteOrder(1, connectionPool);

        List<Order> actualOrdersBefore = OrderMapper.getAllOrders(connectionPool);
        Assertions.assertEquals(1, actualOrdersBefore.size());
        Assertions.assertEquals(expectedOrders.get(1), actualOrdersBefore.get(0));
    }

    @Test
    void setOrderStatusTest() throws DatabaseException {
        OrderMapper.setOrderStatus(1, "complete", connectionPool);

        String actualOrderStatus = OrderMapper.getOrderById(1, connectionPool).getStatus();
        Assertions.assertEquals("Complete", actualOrderStatus);
    }
}