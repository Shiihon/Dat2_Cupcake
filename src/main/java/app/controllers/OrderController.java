package app.controllers;

import app.entities.CupcakePart;
import app.entities.OrderItem;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CupcakeMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        // User Routes
        app.post("addtocart", ctx -> addToCart(ctx, connectionPool));
        app.get("myorders", ctx -> viewMyOrders(ctx, connectionPool));
        app.get("viewcart", ctx -> viewMyCart(ctx, connectionPool));
        app.post("ordernow", ctx -> placeOrder());
        app.post("cancelorderinoverview", ctx -> cancelOrderInOverview());
        app.get("backtoordersite", ctx -> ctx.redirect("/user-frontpage"));
        app.post("cancelorder", ctx -> cancelOrder());
        app.get("/user-frontpage", ctx -> loadCupcakeParts(ctx, connectionPool));

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

    private static void placeOrder() {
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
        ctx.render("admin-frontpage.html");
    }

    private static void viewCustomers(Context ctx, ConnectionPool connectionPool) {
        ctx.render("admin-customers.html");
    }

    private static void viewCustomerOrders(Context ctx, ConnectionPool connectionPool) {
        ctx.render("admin-customer-orders.html");
    }
}