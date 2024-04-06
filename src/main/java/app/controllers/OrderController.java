package app.controllers;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;

public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool){
        app.post("addtobasket", ctx -> addToBasket(ctx, connectionPool));
        app.post("myorders", ctx -> viewMyOrders());
        app.post("basket", ctx -> viewMyBasket());
        app.post("ordernow", ctx -> placeOrder());
        app.post("cancelorderinoverview", ctx -> cancelOrderInOverview());
        app.post("orderisready", ctx -> orderReadyToPickup());
        app.post("rejectorder", ctx -> rejectOrder());
        app.get("backtoordersite", ctx -> ctx.render("user-frontpage.html"));
        app.post("cancelorder", ctx -> cancelOrder());
    }

    private static void addToBasket(Context ctx, ConnectionPool connectionpool) {
        String bottomId = ctx.formParam("bottom");
        String topId = ctx.formParam("topping");
        String amountN = ctx.formParam("amount");
        int amount = Integer.parseInt(amountN);

        List<OrderItem> basket = ctx.sessionAttribute("basket");
        if (basket == null) {
            basket = new ArrayList<>();
        }

        basket.add(new OrderItem(Integer.parseInt(bottomId), Integer.parseInt(topId), amount));
        ctx.sessionAttribute("basket", basket);

        ctx.redirect("user-frontpage.html");
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
