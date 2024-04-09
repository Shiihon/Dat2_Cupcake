package app.controllers;

import app.entities.CupcakePart;
import app.entities.Order;
import app.entities.OrderItem;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CupcakeMapper;
import app.persistence.OrderMapper;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        // User Routes
        app.post("addtocart", ctx -> addToCart(ctx, connectionPool));
        app.post("cancelorderinoverview", ctx -> cancelOrderInOverview());
        app.get("backtoordersite", ctx -> ctx.redirect("/user-frontpage"));
        app.post("cancelorder", ctx -> cancelOrder());

        app.get("ordernow", ctx -> placeOrder(ctx, connectionPool));
        app.get("/user-frontpage", ctx -> loadCupcakeParts(ctx, connectionPool));
        app.get("backtoordersite", ctx -> ctx.redirect("/user-frontpage"));
        app.get("myorders", ctx -> viewMyOrders(ctx, connectionPool));
        app.get("viewcart", ctx -> viewMyCart(ctx, connectionPool));

        // Admin routes
        app.post("delete-order", ctx -> deleteOrder());
        app.post("set-order-ready", ctx -> orderReadyToPickup());
        app.get("/active-customers-orders", ctx -> viewAllCustomersOrders(ctx, connectionPool));
        app.get("/customers", ctx -> viewCustomers(ctx, connectionPool));
        app.get("/customer-orders", ctx -> viewCustomerOrders(ctx, connectionPool));
    }

    private static void loadCupcakeParts(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        List<CupcakePart> bottoms = CupcakeMapper.getAllCupcakeBottoms(connectionPool);
        List<CupcakePart> tops = CupcakeMapper.getAllCupcakeTops(connectionPool);
        ctx.attribute("bottoms", bottoms);
        ctx.attribute("tops", tops);
        ctx.render("user-frontpage.html");
    }

    public static void addToCart(Context ctx, ConnectionPool connectionPool) throws DatabaseException {

        ctx.sessionAttribute("currentUser");

        try {
            int bottomId = Integer.parseInt(ctx.formParam("bottom"));
            int topId = Integer.parseInt(ctx.formParam("top"));
            int amount = Integer.parseInt(ctx.formParam("amount"));

            CupcakePart bottomPart = CupcakeMapper.getCupcakeBottomById(bottomId, connectionPool);
            CupcakePart topPart = CupcakeMapper.getCupcakeTopById(topId, connectionPool);

            OrderItem newItem = new OrderItem(bottomPart, topPart, amount);

            List<OrderItem> basket = ctx.sessionAttribute("basket");
            if (basket == null) {
                basket = new ArrayList<>();
            }

            basket.add(newItem);

            ctx.sessionAttribute("basket", basket);
            ctx.redirect("/user-frontpage");

        } catch (NumberFormatException | DatabaseException e) {
            ctx.attribute("Error adding to cart: " + e.getMessage());
            ctx.render("user-frontpage.html");
        }
    }

    private static void viewMyOrders(Context ctx, ConnectionPool connectionPool) {
        ctx.render("my-orders.html");
    }

    private static void viewMyCart(Context ctx, ConnectionPool connectionPool) {
        List<OrderItem> basket = ctx.sessionAttribute("basket");
        if (basket == null) {
            basket = new ArrayList<>();
        }

        int totalPrice = calculateTotalPrice(basket);
        ctx.attribute("basket", basket);
        ctx.attribute("totalPrice", totalPrice);
        ctx.render("order-overview.html");
    }

    public static int calculateTotalPrice(List<OrderItem> basket) {
        int totalPrice = 0;
        for (OrderItem item : basket) {
            totalPrice += item.getTotalItemPrice();
        }
        return totalPrice;
    }

    public static void placeOrder(Context ctx, ConnectionPool connectionPool) {
        try {

            User userId = ctx.sessionAttribute("currentUser");
            List<OrderItem> basket = ctx.sessionAttribute("basket");

            if (userId != null && basket != null && !basket.isEmpty()) {

                Order newOrder = new Order(-1,userId.getUserId(), basket, "In Progress", LocalDateTime.now());

                OrderMapper.createOrder(newOrder, connectionPool);

                ctx.sessionAttribute("basket", new ArrayList<OrderItem>());
                ctx.redirect("/pop-up.html");
            } else {
                ctx.result("Basket empty or user not logged in");
            }
        } catch (DatabaseException e) {
            ctx.result("Error placing order." + e.getMessage());
        }
    }

    private static void cancelOrderInOverview() {
    }

    private static void orderReadyToPickup() {
    }

    private static void deleteOrder() {
    }

    private static void cancelOrder() {
    }

    private static void viewAllCustomersOrders(Context ctx, ConnectionPool connectionPool) {
        try {
            List<Order> activeOrders = OrderMapper.getAllOrdersByStatus("In Progress", connectionPool);
            Map<Order, User> userOrderMap = new LinkedHashMap<>();
            Map<Integer, User> userMap = new HashMap<>();

            for (Order order : activeOrders) {
                if (!userMap.containsKey(order.getUserId())) {
                    userMap.put(order.getUserId(), UserMapper.getUserById(order.getUserId(), connectionPool));
                }

                userOrderMap.put(order, userMap.get(order.getUserId()));
            }

            ctx.attribute("orders", userOrderMap);
            ctx.render("admin-frontpage.html");
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    private static void viewCustomers(Context ctx, ConnectionPool connectionPool) {
        try {
            List<User> customers = UserMapper.getAllUsersByRole("customer", connectionPool);

            ctx.attribute("customers", customers);
            ctx.render("admin-customers.html");
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    private static void viewCustomerOrders(Context ctx, ConnectionPool connectionPool) {
        try {
            int customerId = Integer.parseInt(Objects.requireNonNull(ctx.queryParam("customerId")));

            User customer = UserMapper.getUserById(customerId, connectionPool);
            List<Order> customerOrders = OrderMapper.getAllUserOrders(customer.getUserId(), connectionPool);

            ctx.attribute("customer", customer);
            ctx.attribute("orders", customerOrders);
            ctx.render("admin-customer-orders.html");
        } catch (DatabaseException e) {
            e.printStackTrace();
        } catch (NullPointerException | NumberFormatException e) {
            ctx.redirect("/customers");
        }
    }
}