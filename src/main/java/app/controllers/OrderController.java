package app.controllers;

import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        // User Routes
        app.post("addtobasket", ctx -> addToBasket(ctx, connectionPool));
        app.post("myorders", ctx -> viewMyOrders());
        app.post("basket", ctx -> viewMyBasket());
        app.post("ordernow", ctx -> placeOrder());
        app.post("cancelorderinoverview", ctx -> cancelOrderInOverview());
        app.get("backtoordersite", ctx -> ctx.render("user-frontpage.html"));
        app.post("cancelorder", ctx -> cancelOrder());

        // Admin routes
        app.post("delete-order", ctx -> deleteOrder());
        app.post("set-order-ready", ctx -> orderReadyToPickup());
        app.get("/view-all-customers-orders", ctx -> viewAllCustomersOrders(ctx, connectionPool));
        app.get("/customers", ctx -> viewCustomers(ctx, connectionPool));
        app.get("/customer-orders", ctx -> viewCustomerOrders(ctx, connectionPool));
    }

    private static void addToBasket(Context ctx, ConnectionPool connectionpool) {
    }

    private static void viewMyOrders() {
    }

    private static void viewMyBasket() {
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
