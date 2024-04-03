package app.controllers;


import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.sql.Connection;

public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool connectionpool){
        app.post("addtobasket", ctx -> addToBasket(ctx, connectionpool));
        app.post("myorders", ctx -> viewMyOrders());
        app.post("basket", ctx -> viewMyBasket());
        app.post("ordernow", ctx -> placeOrder());
        app.post("cancelovervieworder", ctx -> cancelOrderInOverview());
        app.post("orderisready", ctx -> orderReadyToPickup());
        app.post("rejectorder", ctx -> rejectOrder());
        app.get("backtoordersite", ctx -> ctx.render("user-frontpage.html"));
        app.post("cancelorder", ctx -> cancelOrder());
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

    private static void rejectOrder() {
    }

    private static void cancelOrder() {
    }

}
